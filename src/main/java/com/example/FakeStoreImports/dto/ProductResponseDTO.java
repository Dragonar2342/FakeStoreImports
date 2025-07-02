package com.example.FakeStoreImports.dto;

import com.example.FakeStoreImports.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для ответа с информацией о товаре")
public class ProductResponseDTO {
    @Schema(description = "Уникальный идентификатор товара", example = "1")
    private Long id;

    @Schema(description = "Название товара", example = "Смартфон")
    private String title;

    @Schema(description = "Цена товара", example = "999.99")
    private BigDecimal price;

    @Schema(description = "Описание товара", example = "Новый флагманский смартфон")
    private String description;

    @Schema(description = "Категория товара")
    private CategoryDTO category;

    @Schema(description = "URL изображения товара", example = "https://example.com/phone.jpg")
    private String image;

    @Schema(description = "Рейтинг товара")
    private RatingDTO rating;

    @Schema(hidden = true)
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