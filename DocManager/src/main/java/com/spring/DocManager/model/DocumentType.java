package com.spring.DocManager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentType {
    DOCUMENT("Документ", 14),
    PASSPORT("Паспорт поверки прибора учета", 30),
    CERTIFICATE("Удостоверение", 14),
    PASSWORD("Пароль", 2);

    private final String displayName;
    private final int warningDays;
}
