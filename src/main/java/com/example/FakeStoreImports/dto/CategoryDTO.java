package com.example.FakeStoreImports.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для категории товара")
public class CategoryDTO {
    @Schema(description = "Уникальный идентификатор категории", example = "1")
    private Long id;

    @Schema(description = "Название категории",
            example = "electronics",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}