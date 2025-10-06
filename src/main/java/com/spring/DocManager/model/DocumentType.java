package com.spring.DocManager.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentType {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String displayName;

    @Positive(message = "Срок предупреждения должен быть больше 0")
    private int warningDays;


}
