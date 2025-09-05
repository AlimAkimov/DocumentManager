package com.spring.DocManager.service;

import com.spring.DocManager.model.Document;
import com.spring.DocManager.model.DocumentType;
import com.spring.DocManager.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document document1;
    private Document document2;

    @BeforeEach
    void setUp() {
        document1 = new Document();
        document1.setId(1L);
        document1.setName("Документ 1");
        document1.setType(DocumentType.DOCUMENT);
        document1.setIssueDate(LocalDate.of(2025, 8, 1));
        document1.setExpirationDate(LocalDate.of(2025, 9, 15));

        document2 = new Document();
        document2.setId(2L);
        document2.setName("Документ 2");
        document2.setType(DocumentType.PASSWORD);
        document2.setIssueDate(LocalDate.of(2025, 9, 1));
        document2.setExpirationDate(LocalDate.of(2025, 9, 6));
    }

    @Test
    void testFindAllDocuments() {
        List<Document> documents = Arrays.asList(document1, document2);
        when(documentRepository.findAll()).thenReturn(documents);

        List<Document> result = documentService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(document1));
        assertTrue(result.contains(document2));
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    void testFindDocumentById_WhenExists() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document1));

        Optional<Document> result = documentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(document1, result.get());
        verify(documentRepository, times(1)).findById(1L);
    }

    @Test
    void testFindDocumentById_WhenNotExists() {
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Document> result = documentService.findById(999L);

        assertFalse(result.isPresent());
        verify(documentRepository, times(1)).findById(999L);
    }

    @Test
    void testSaveDocument() {
        // Arrange
        when(documentRepository.save(document1)).thenReturn(document1);

        Document result = documentService.save(document1);

        assertEquals(document1, result);
        verify(documentRepository, times(1)).save(document1);
    }

    @Test
    void testDeleteDocument() {
        documentService.deleteById(1L);

        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetExpiringDocuments() {
        LocalDate today = LocalDate.of(2025, 9, 5);

        when(documentRepository.findExpiringDocuments(
                today.plusDays(14),
                today.plusDays(30),
                today.plusDays(2)))
                .thenReturn(Arrays.asList(document1, document2));

        try (MockedStatic<LocalDate> mockedDate = Mockito.mockStatic(LocalDate.class)) {
            mockedDate.when(LocalDate::now).thenReturn(today);

            List<Document> result = documentService.getExpiringDocuments();

            assertEquals(2, result.size());
            assertTrue(result.contains(document1));
            assertTrue(result.contains(document2));
            verify(documentRepository, times(1))
                    .findExpiringDocuments(today.plusDays(14), today.plusDays(30), today.plusDays(2));
        }
    }

@Test
    void testUpdateDocument_WhenExists() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document1));
        when(documentRepository.save(document1)).thenReturn(document1);

        Document updatedDocument = new Document();
        updatedDocument.setId(1L);
        updatedDocument.setName("updated document");
        updatedDocument.setType(DocumentType.CERTIFICATE);
        updatedDocument.setIssueDate(LocalDate.of(2025, 8, 10));
        updatedDocument.setExpirationDate(LocalDate.of(2025, 9, 20));

        documentService.updateDocument(updatedDocument);

        assertEquals("updated document", document1.getName());
        assertEquals(DocumentType.CERTIFICATE, document1.getType());
        assertEquals(LocalDate.of(2025, 8, 10), document1.getIssueDate());
        assertEquals(LocalDate.of(2025, 9, 20), document1.getExpirationDate());
        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).save(document1);
    }

    @Test
    void testUpdateDocument_WhenNotExists() {
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());
        Document document = new Document();
        document.setId(999L);

        assertThrows(IllegalArgumentException.class, () -> documentService.updateDocument(document));
        verify(documentRepository, times(1)).findById(999L);
        verify(documentRepository, never()).save(any());
    }
}