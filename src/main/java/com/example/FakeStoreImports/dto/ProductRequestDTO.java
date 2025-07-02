package com.example.FakeStoreImports.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для запроса на создание/обновление товара")
public class ProductRequestDTO {
    @Schema(description = "Название товара",
            example = "Смартфон",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Цена товара",
            example = "999.99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Описание товара",
            example = "Новый флагманский смартфон")
    private String description;

    @Schema(description = "Название категории товара",
            example = "electronics",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryName;

    @Schema(description = "URL изображения товара",
            example = "https://example.com/phone.jpg")
    private String image;

    @Schema(description = "Рейтинг товара")
    private RatingDTO rating;
}