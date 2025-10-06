package com.spring.DocManager.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class Document {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private DocumentType type;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private Group group;

}
