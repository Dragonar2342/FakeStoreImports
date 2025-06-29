package com.example.FakeStoreImports.dto;

import com.example.FakeStoreImports.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private CategoryDTO category;
    private String image;
    private RatingDTO rating;

    public static ProductResponseDTO fromEntity(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            dto.setCategory(new CategoryDTO(
                    product.getCategory().getId(),
                    product.getCategory().getName()
            ));
        }
        dto.setImage(product.getImage());

        if (product.getRating() != null) {
            dto.setRating(new RatingDTO(
                    product.getRating().getRate(),
                    product.getRating().getCount()
            ));
        }

        return dto;
    }
}
