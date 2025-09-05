package com.spring.DocManager.repository;

import com.spring.DocManager.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT d FROM Document d WHERE " +
            "(d.type = 'DOCUMENT' AND d.expirationDate <= :nowPlus14) OR " +
            "(d.type = 'PASSPORT' AND d.expirationDate <= :nowPlus30) OR " +
            "(d.type = 'CERTIFICATE' AND d.expirationDate <= :nowPlus14) OR " +
            "(d.type = 'PASSWORD' AND d.expirationDate <= :nowPlus2)")
    List<Document> findExpiringDocuments(@Param("nowPlus14") LocalDate nowPlus14,
                                         @Param("nowPlus30") LocalDate nowPlus30,
                                         @Param("nowPlus2") LocalDate nowPlus2);
}
