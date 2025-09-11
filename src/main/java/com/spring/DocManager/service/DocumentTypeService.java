package com.spring.DocManager.service;

import com.spring.DocManager.model.DocumentType;
import com.spring.DocManager.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeService {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }

    public DocumentType findById(Long id) {
        return documentTypeRepository.findById(id).orElseThrow(() ->
        new IllegalArgumentException("Тип документа не найден"));
    }

    public DocumentType save(DocumentType type) {
        return documentTypeRepository.save(type);
    }

    public void deleteById(Long id) {
        documentTypeRepository.deleteById(id);
    }
}
