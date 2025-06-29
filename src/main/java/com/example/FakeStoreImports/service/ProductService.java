package com.example.FakeStoreImports.service;

import com.example.FakeStoreImports.dto.ProductDTO;
import com.example.FakeStoreImports.dto.ProductRequestDTO;
import com.example.FakeStoreImports.dto.ProductResponseDTO;
import com.example.FakeStoreImports.entity.Category;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.entity.Rating;
import com.example.FakeStoreImports.repository.CategoryRepository;
import com.example.FakeStoreImports.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WebClient webClient;

    @Transactional
    public void importProductsFromExternalApi() {
        List<ProductDTO> productDTOs = webClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList()
                .block();

        if (productDTOs != null) {
            for (ProductDTO dto : productDTOs) {
                if (!productRepository.existsById(dto.getId())) {
                    saveProductFromDTO(dto);
                }
            }
        }
    }

    private void saveProductFromDTO(ProductDTO dto) {
        Category category = categoryRepository.findByName(dto.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(dto.getCategory());
                    return categoryRepository.save(newCategory);
                });

        Rating rating = new Rating();
        rating.setRate(dto.getRating().getRate());
        rating.setCount(dto.getRating().getCount());

        Product product = new Product();
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setImage(dto.getImage());
        product.setRating(rating);

        productRepository.save(product);
    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponseDTO::fromEntity);
    }

    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        // Преобразуем DTO в сущность Product
        Product product = new Product();
        product.setTitle(requestDTO.getTitle());
        product.setPrice(requestDTO.getPrice());
        product.setDescription(requestDTO.getDescription());
        product.setImage(requestDTO.getImage());

        // Обрабатываем категорию (находим или создаем новую)
        Category category = categoryRepository.findByName(requestDTO.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(requestDTO.getCategoryName());
                    return categoryRepository.save(newCategory);
                });
        product.setCategory(category);

        // Обрабатываем рейтинг (если есть)
        if (requestDTO.getRating() != null) {
            Rating rating = new Rating();
            rating.setRate(requestDTO.getRating().getRate());
            rating.setCount(requestDTO.getRating().getCount());
            product.setRating(rating);
        }

        // Сохраняем продукт и возвращаем DTO
        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setTitle(productDetails.getTitle());
                    product.setPrice(productDetails.getPrice());
                    product.setDescription(productDetails.getDescription());
                    product.setCategory(productDetails.getCategory());
                    product.setImage(productDetails.getImage());
                    if (productDetails.getRating() != null) {
                        if (product.getRating() == null) {
                            product.setRating(new Rating());
                        }
                        product.getRating().setRate(productDetails.getRating().getRate());
                        product.getRating().setCount(productDetails.getRating().getCount());
                    }
                    return productRepository.save(product);
                });
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<String> getAllCategories() {
        return categoryRepository.findAllUniqueCategoryNames();
    }
}
