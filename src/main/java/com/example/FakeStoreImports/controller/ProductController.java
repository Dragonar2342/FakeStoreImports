package com.example.FakeStoreImports.controller;

import com.example.FakeStoreImports.dto.ProductRequestDTO;
import com.example.FakeStoreImports.dto.ProductResponseDTO;
import com.example.FakeStoreImports.dto.RatingDTO;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Товары (API)", description = "REST API для управления товарами")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/import")
    @Operation(summary = "Импорт товаров", description = "Импортирует товары из внешнего API FakeStore")
    @ApiResponse(responseCode = "200", description = "Товары успешно импортированы")
    public ResponseEntity<String> importProducts() {
        productService.importProductsFromExternalApi();
        return ResponseEntity.ok("Продукты успешно импортированы");
    }

    @GetMapping
    @Operation(summary = "Получить все товары", description = "Возвращает список товаров с пагинацией")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(schema = @Schema(implementation = Page.class)))
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    @Operation(summary = "Фильтр по цене", description = "Фильтрует товары по диапазону цен")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByPriceRange(
            @Parameter(description = "Минимальная цена") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Максимальная цена") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Параметры пагинации") Pageable pageable) {

        Page<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        Page<ProductResponseDTO> dtos = products.map(ProductResponseDTO::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID", description = "Возвращает детали конкретного товара")
    @ApiResponse(responseCode = "200", description = "Товар найден")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ProductResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать товар", description = "Создает новый товар")
    @ApiResponse(responseCode = "200", description = "Товар успешно создан")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(requestDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить товар", description = "Изменяет сущетсвующий товар")
    @ApiResponse(responseCode = "200", description = "Товар успешно изменён")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> updatedProduct = productService.updateProduct(id, productDetails);
        return updatedProduct.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление товара", description = "Удаляет сущетсвующий товар")
    @ApiResponse(responseCode = "200", description = "Товар успешно удалён")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @PathVariable String categoryName,
            Pageable pageable) {
        Page<ProductResponseDTO> products = productService.getProductsByCategory(categoryName, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sorted")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsSorted(
            @RequestParam(required = false) String priceDirection,
            @RequestParam(required = false) String categoryDirection,
            Pageable pageable) {

        Page<ProductResponseDTO> products = productService.getProductsSorted(
                priceDirection, categoryDirection, pageable);
        return ResponseEntity.ok(products);
    }
}
