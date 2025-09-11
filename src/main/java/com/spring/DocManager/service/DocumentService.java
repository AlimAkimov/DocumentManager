package com.spring.DocManager.service;

import com.spring.DocManager.model.Document;
import com.spring.DocManager.model.DocumentType;
import com.spring.DocManager.repository.DocumentRepository;
import com.spring.DocManager.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    public List<Document> getExpiringDocuments() {
        LocalDate now = LocalDate.now();
        List<DocumentType> types = documentTypeRepository.findAll();

        return types.stream()
                .flatMap(type -> {
                    LocalDate expirationDate = now.plusDays(type.getWarningDays());
                    return documentRepository.findByExpirationDateLessThanEqual(expirationDate)
                            .stream()
                            .filter(doc -> doc.getType().getId().equals(type.getId()));
                })
                .collect(Collectors.toList());
    }

    public void updateDocument(Document document) {
        Document existingDocument = findById(document.getId())
                .orElseThrow(() -> new IllegalArgumentException("Документ не найден"));
        existingDocument.setName(document.getName());
        existingDocument.setType(document.getType());
        existingDocument.setIssueDate(document.getIssueDate());
        existingDocument.setExpirationDate(document.getExpirationDate());
        existingDocument.setGroup(document.getGroup());

        save(existingDocument);
    }
}
