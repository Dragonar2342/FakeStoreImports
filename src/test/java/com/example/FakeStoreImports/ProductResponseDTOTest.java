package com.example.FakeStoreImports;

import com.example.FakeStoreImports.dto.ProductResponseDTO;
import com.example.FakeStoreImports.entity.Category;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.entity.Rating;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseDTOTest {

    @Test
    void fromEntity_ShouldConvertCorrectly() {
        Category category = new Category();
        category.setId(1L);
        category.setName("electronics");

        Rating rating = new Rating();
        rating.setRate(4.5);
        rating.setCount(120);

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(BigDecimal.valueOf(9.99));
        product.setDescription("Test Description");
        product.setCategory(category);
        product.setImage("image.jpg");
        product.setRating(rating);

        ProductResponseDTO dto = ProductResponseDTO.fromEntity(product);

        assertEquals(1L, dto.getId());
        assertEquals("Test Product", dto.getTitle());
        assertEquals(BigDecimal.valueOf(9.99), dto.getPrice());
        assertEquals("electronics", dto.getCategory().getName());
        assertEquals(4.5, dto.getRating().getRate());
    }
}
