package com.example.FakeStoreImports;

import com.example.FakeStoreImports.entity.Category;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.repository.CategoryRepository;
import com.example.FakeStoreImports.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByPriceBetween_ShouldReturnFilteredProducts() {
        Category category = new Category();
        category.setName("electronics");
        categoryRepository.save(category);

        Product product1 = new Product();
        product1.setTitle("Product 1");
        product1.setPrice(BigDecimal.valueOf(15.99));
        product1.setCategory(category);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setTitle("Product 2");
        product2.setPrice(BigDecimal.valueOf(25.99));
        product2.setCategory(category);
        productRepository.save(product2);

        Page<Product> result = productRepository.findByPriceBetween(
                BigDecimal.valueOf(10), BigDecimal.valueOf(20), PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals("Product 1", result.getContent().get(0).getTitle());
    }

    @Test
    void findByCategoryName_ShouldReturnProducts() {
        Category category = new Category();
        category.setName("electronics");
        categoryRepository.save(category);

        Product product = new Product();
        product.setTitle("Test Product");
        product.setCategory(category);
        productRepository.save(product);

        Page<Product> result = productRepository.findByCategoryName(
                "electronics", PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals("Test Product", result.getContent().get(0).getTitle());
    }
}
