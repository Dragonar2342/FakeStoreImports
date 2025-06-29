package com.example.FakeStoreImports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private String title;
    private BigDecimal price;
    private String description;
    private String categoryName;
    private String image;
    private RatingDTO rating;
}
