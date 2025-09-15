package com.spring.DocManager.repository;

import static nu.studer.sample.tables.DocumentTypes.DOCUMENT_TYPES;

import com.spring.DocManager.model.DocumentType;
import lombok.RequiredArgsConstructor;
import nu.studer.sample.tables.records.DocumentTypesRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentTypeRepository {

    private final DSLContext dsl;

    public List<DocumentType> findAll() {
        return dsl.selectFrom(DOCUMENT_TYPES)
                .orderBy(DOCUMENT_TYPES.NAME.asc())
                .fetch()
                .map(this::mapToDocumentType);
    }

    public Optional<DocumentType> findById(Long id) {
        return Optional.ofNullable(
                dsl.selectFrom(DOCUMENT_TYPES)
                        .where(DOCUMENT_TYPES.ID.eq(id))
                        .fetchOne()
        ).map(this::mapToDocumentType);
    }

    public DocumentType save(DocumentType documentType) {
        if (documentType.getId() == null) {
            DocumentTypesRecord savedRecord = dsl.insertInto(DOCUMENT_TYPES)
                    .set(setDocumentTypeFields(documentType))
                    .returning()
                    .fetchOptional()
                    .orElseThrow(() -> new IllegalStateException("Не удалось сохранить тип документа"));
            return mapToDocumentType(savedRecord);
        } else {
            dsl.update(DOCUMENT_TYPES)
                    .set(setDocumentTypeFields(documentType))
                    .where(DOCUMENT_TYPES.ID.eq(documentType.getId()))
                    .execute();
            return documentType;
        }
    }

    public void deleteById(Long id) {
        dsl.deleteFrom(DOCUMENT_TYPES)
                .where(DOCUMENT_TYPES.ID.eq(id))
                .execute();
    }

    private DocumentType mapToDocumentType(DocumentTypesRecord record) {
        DocumentType documentType = new DocumentType();
        documentType.setId(record.getId());
        documentType.setName(record.getName());
        documentType.setDisplayName(record.getDisplayName());
        documentType.setWarningDays(record.getWarningDays());
        return documentType;
    }

    private Map<Field<?>, Object> setDocumentTypeFields(DocumentType documentType) {
        Map<Field<?>, Object> fields = new HashMap<>();
        fields.put(DOCUMENT_TYPES.NAME, documentType.getName());
        fields.put(DOCUMENT_TYPES.DISPLAY_NAME, documentType.getDisplayName());
        fields.put(DOCUMENT_TYPES.WARNING_DAYS, documentType.getWarningDays());
        return fields;
    }


}
