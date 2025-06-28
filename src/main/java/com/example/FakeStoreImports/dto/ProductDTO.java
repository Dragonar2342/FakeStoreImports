package com.example.FakeStoreImports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;
    private RatingDTO rating;
}
