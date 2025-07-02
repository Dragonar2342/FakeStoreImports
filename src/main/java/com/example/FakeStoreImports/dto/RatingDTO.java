package com.example.FakeStoreImports.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для рейтинга товара")
public class RatingDTO {
    @Schema(description = "Средний рейтинг товара",
            example = "4.5",
            minimum = "0",
            maximum = "5")
    private Double rate;

    @Schema(description = "Количество оценок товара",
            example = "120",
            minimum = "0")
    private Integer count;
}