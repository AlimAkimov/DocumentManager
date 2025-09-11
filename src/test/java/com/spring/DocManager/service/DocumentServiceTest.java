package com.spring.DocManager.service;

import com.spring.DocManager.model.Document;
import com.spring.DocManager.model.DocumentType;
import com.spring.DocManager.repository.DocumentRepository;
import com.spring.DocManager.repository.DocumentTypeRepository;
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

    @Mock
    private DocumentTypeRepository documentTypeRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document document1;
    private Document document2;
    private DocumentType typeDocument;
    private DocumentType typePassword;

    @BeforeEach
    void setUp() {
        typeDocument = new DocumentType();
        typeDocument.setId(1L);
        typeDocument.setName("DOCUMENT");
        typeDocument.setWarningDays(14);

        typePassword = new DocumentType();
        typePassword.setId(2L);
        typePassword.setName("PASSWORD");
        typePassword.setWarningDays(2);

        document1 = new Document();
        document1.setId(1L);
        document1.setName("Документ 1");
        document1.setType(typeDocument);
        document1.setIssueDate(LocalDate.of(2025, 8, 1));
        document1.setExpirationDate(LocalDate.of(2025, 9, 15));

        document2 = new Document();
        document2.setId(2L);
        document2.setName("Документ 2");
        document2.setType(typePassword);
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

        when(documentTypeRepository.findAll()).thenReturn(Arrays.asList(typeDocument, typePassword));
        when(documentRepository.findByExpirationDateLessThanEqual(today.plusDays(14)))
                .thenReturn(List.of(document1));
        when(documentRepository.findByExpirationDateLessThanEqual(today.plusDays(2)))
                .thenReturn(List.of(document2));

        try (MockedStatic<LocalDate> mockedDate = Mockito.mockStatic(LocalDate.class)) {
            mockedDate.when(LocalDate::now).thenReturn(today);

            List<Document> result = documentService.getExpiringDocuments();

            assertEquals(2, result.size());
            assertTrue(result.contains(document1));
            assertTrue(result.contains(document2));
        }
    }

    @Test
    void testUpdateDocument_WhenExists() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document1));
        when(documentRepository.save(document1)).thenReturn(document1);

        DocumentType typeCertificate = new DocumentType();
        typeCertificate.setId(3L);
        typeCertificate.setName("CERTIFICATE");
        typeCertificate.setWarningDays(14);

        Document updatedDocument = new Document();
        updatedDocument.setId(1L);
        updatedDocument.setName("updated document");
        updatedDocument.setType(typeCertificate);
        updatedDocument.setIssueDate(LocalDate.of(2025, 8, 10));
        updatedDocument.setExpirationDate(LocalDate.of(2025, 9, 20));

        documentService.updateDocument(updatedDocument);

        assertEquals("updated document", document1.getName());
        assertEquals(typeCertificate, document1.getType());
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