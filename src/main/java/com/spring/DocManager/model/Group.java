package com.spring.DocManager.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class Group {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String color;

}
