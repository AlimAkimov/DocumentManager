package com.spring.DocManager.repository;

import com.spring.DocManager.model.Document;
import lombok.RequiredArgsConstructor;
import nu.studer.sample.tables.records.DocumentsRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nu.studer.sample.tables.Documents.DOCUMENTS;

@Repository
@RequiredArgsConstructor
public class DocumentRepository {

    private final DSLContext dsl;
    private final GroupRepository groupRepository;
    private final DocumentTypeRepository documentTypeRepository;

    public List<Document> findAll() {
        return dsl.selectFrom(DOCUMENTS)
                .orderBy(DOCUMENTS.NAME.asc())
                .fetch()
                .map(this::mapToDocument);
    }

    public Optional<Document> findById(Long id) {
        return Optional.ofNullable(
                dsl.selectFrom(DOCUMENTS)
                        .where(DOCUMENTS.ID.eq(id))
                        .fetchOne()
        ).map(this::mapToDocument);
    }

    public Document save(Document document) {
        if (document.getId() == null) {
            DocumentsRecord savedRecord = dsl.insertInto(DOCUMENTS)
                    .set(setDocumentFields(document))
                    .returning()
                    .fetchOptional()
                    .orElseThrow(() -> new IllegalStateException("Не удалось сохранить документ"));
            return mapToDocument(savedRecord);
        } else {
            dsl.update(DOCUMENTS)
                    .set(setDocumentFields(document))
                    .where(DOCUMENTS.ID.eq(document.getId()))
                    .execute();
            return document;
        }
    }

    public void deleteById(Long id) {
        dsl.deleteFrom(DOCUMENTS)
                .where(DOCUMENTS.ID.eq(id))
                .execute();
    }

    public List<Document> findByExpirationDateLessThanEqual(LocalDate date) {
        return dsl.selectFrom(DOCUMENTS)
                .where(DOCUMENTS.EXPIRATION_DATE.lessOrEqual(date))
                .orderBy(DOCUMENTS.NAME.asc())
                .fetch()
                .map(this::mapToDocument);
    }

    private Document mapToDocument(DocumentsRecord record) {
        Document document = new Document();
        document.setId(record.getId());
        document.setName(record.getName());
        document.setIssueDate(record.getIssueDate());
        document.setExpirationDate(record.getExpirationDate());

        document.setGroup(
                groupRepository.findById(record.getGroupId())
                        .orElse(null)
        );
        document.setType(
                documentTypeRepository.findById(record.getTypeId())
                        .orElse(null)
        );
        return document;
    }

    private Map<Field<?>, Object> setDocumentFields(Document document) {
        Map<Field<?>, Object> fields = new HashMap<>();
        fields.put(DOCUMENTS.NAME, document.getName());
        fields.put(DOCUMENTS.TYPE_ID, document.getType().getId());
        fields.put(DOCUMENTS.ISSUE_DATE, document.getIssueDate());
        fields.put(DOCUMENTS.EXPIRATION_DATE, document.getExpirationDate());
        fields.put(DOCUMENTS.GROUP_ID, document.getGroup().getId());
        return fields;
    }
}
