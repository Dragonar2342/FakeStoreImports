package com.example.FakeStoreImports;

import com.example.FakeStoreImports.dto.ProductDTO;
import com.example.FakeStoreImports.dto.ProductRequestDTO;
import com.example.FakeStoreImports.dto.ProductResponseDTO;
import com.example.FakeStoreImports.dto.RatingDTO;
import com.example.FakeStoreImports.entity.Category;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.entity.Rating;
import com.example.FakeStoreImports.repository.CategoryRepository;
import com.example.FakeStoreImports.repository.ProductRepository;
import com.example.FakeStoreImports.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("electronics");

        Rating rating = new Rating();
        rating.setRate(4.5);
        rating.setCount(120);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setTitle("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(9.99));
        testProduct.setDescription("Test Description");
        testProduct.setCategory(category);
        testProduct.setImage("image.jpg");
        testProduct.setRating(rating);

        testProductDTO = new ProductDTO(
                1L,
                "Test Product",
                BigDecimal.valueOf(9.99),
                "Test Description",
                "electronics",
                "image.jpg",
                new RatingDTO(4.5, 120)
        );
    }

    @Test
    void importProductsFromExternalApi_ShouldSaveNewProducts() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProductDTO.class)).thenReturn(Flux.just(testProductDTO));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(categoryRepository.findByName("electronics")).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenReturn(new Category());
        when(productRepository.save(any())).thenReturn(testProduct);

        productService.importProductsFromExternalApi();

        verify(productRepository, times(1)).save(any());
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(testProduct));

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<ProductResponseDTO> result = productService.getAllProducts(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Test Product", result.getContent().get(0).getTitle());
    }

    @Test
    void getProductsByCategory_ShouldReturnFilteredProducts() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(testProduct));

        when(productRepository.findByCategoryName("electronics", pageable)).thenReturn(page);

        Page<ProductResponseDTO> result = productService.getProductsByCategory("electronics", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("electronics", result.getContent().get(0).getCategory().getName());
    }

    @Test
    void getProductsByPriceRange_ShouldReturnFilteredProducts() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(testProduct));

        when(productRepository.findByPriceBetween(
                BigDecimal.valueOf(10), BigDecimal.valueOf(100), pageable))
                .thenReturn(page);

        Page<Product> result = productService.getProductsByPriceRange(
                BigDecimal.valueOf(10), BigDecimal.valueOf(100), pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void getProductsSorted_ShouldApplySorting() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("price").ascending());
        Page<Product> page = new PageImpl<>(List.of(testProduct));

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<ProductResponseDTO> result = productService.getProductsSorted(
                "asc", null, PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getTitle());
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        ProductRequestDTO requestDTO = new ProductRequestDTO(
                "New Product",
                BigDecimal.valueOf(19.99),
                "New Description",
                "electronics",
                "new_image.jpg",
                new RatingDTO(4.0, 100)
        );

        when(categoryRepository.findByName("electronics")).thenReturn(Optional.of(new Category()));
        when(productRepository.save(any())).thenReturn(testProduct);

        ProductResponseDTO result = productService.createProduct(requestDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any())).thenReturn(testProduct);

        Optional<Product> result = productService.updateProduct(1L, testProduct);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getTitle());
    }

    @Test
    void deleteProduct_ShouldCallRepository() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllCategories_ShouldReturnCategoryNames() {
        when(categoryRepository.findAllUniqueCategoryNames())
                .thenReturn(List.of("electronics", "clothing"));

        List<String> result = productService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("electronics", result.get(0));
    }
}
