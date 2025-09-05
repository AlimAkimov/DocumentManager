package com.spring.DocManager.repository;

import com.spring.DocManager.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DocumentRepository extends JpaRepository <Document, Long> {
    @Query("SELECT d FROM Document d WHERE d.expirationDate <= :expirationDate")
    List<Document> findExpiringDocumentsByDate(LocalDate expirationDate);
}
