package com.example.FakeStoreImports.controller;

import com.example.FakeStoreImports.dto.ProductRequestDTO;
import com.example.FakeStoreImports.dto.ProductResponseDTO;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @ApiResponse(responseCode = "200", description = "Товары успешно импортированы",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Продукты успешно импортированы\"}")))
    public ResponseEntity<String> importProducts() {
        productService.importProductsFromExternalApi();
        return ResponseEntity.ok("Продукты успешно импортированы");
    }

    @GetMapping
    @Operation(summary = "Получить все товары", description = "Возвращает список товаров с пагинацией")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(schema = @Schema(implementation = ProductPage.class)))
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @Parameter(description = "Параметры пагинации (page, size, sort)") Pageable pageable) {
        Page<ProductResponseDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    @Operation(summary = "Фильтр по цене", description = "Фильтрует товары по диапазону цен")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(schema = @Schema(implementation = ProductPage.class)))
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByPriceRange(
            @Parameter(description = "Минимальная цена", example = "10.0")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Максимальная цена", example = "100.0")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Параметры пагинации") Pageable pageable) {
        Page<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        Page<ProductResponseDTO> dtos = products.map(ProductResponseDTO::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID", description = "Возвращает детали конкретного товара")
    @ApiResponse(responseCode = "200", description = "Товар найден",
            content = @Content(schema = @Schema(implementation = ProductResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "ID товара", example = "1", required = true)
            @PathVariable Long id) {
        return productService.getProductById(id)
                .map(ProductResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать товар", description = "Создает новый товар")
    @ApiResponse(responseCode = "200", description = "Товар успешно создан",
            content = @Content(schema = @Schema(implementation = ProductResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody @Valid
            @Schema(description = "Данные для создания товара")
            ProductRequestDTO requestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(requestDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить товар", description = "Обновляет существующий товар")
    @ApiResponse(responseCode = "200", description = "Товар успешно обновлен",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID товара", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody @Valid
            @Schema(description = "Обновленные данные товара")
            Product productDetails) {
        Optional<Product> updatedProduct = productService.updateProduct(id, productDetails);
        return updatedProduct.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар", description = "Удаляет товар по ID")
    @ApiResponse(responseCode = "204", description = "Товар успешно удален")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID товара", example = "1", required = true)
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    @Operation(summary = "Получить все категории", description = "Возвращает список всех категорий")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(array = @ArraySchema(schema = @Schema(type = "string"))))
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @GetMapping("/category/{categoryName}")
    @Operation(summary = "Товары по категории", description = "Возвращает товары указанной категории")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(schema = @Schema(implementation = ProductPage.class)))
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @Parameter(description = "Название категории", example = "electronics", required = true)
            @PathVariable String categoryName,
            @Parameter(description = "Параметры пагинации") Pageable pageable) {
        Page<ProductResponseDTO> products = productService.getProductsByCategory(categoryName, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sorted")
    @Operation(summary = "Сортировка товаров", description = "Возвращает отсортированный список товаров")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(schema = @Schema(implementation = ProductPage.class)))
    public ResponseEntity<Page<ProductResponseDTO>> getProductsSorted(
            @Parameter(description = "Направление сортировки по цене (asc/desc)", example = "asc")
            @RequestParam(required = false) String priceDirection,
            @Parameter(description = "Направление сортировки по категории (asc/desc)", example = "desc")
            @RequestParam(required = false) String categoryDirection,
            @Parameter(description = "Параметры пагинации") Pageable pageable) {
        Page<ProductResponseDTO> products = productService.getProductsSorted(
                priceDirection, categoryDirection, pageable);
        return ResponseEntity.ok(products);
    }

    @Schema(name = "ProductPage", description = "Страница с товарами")
    private interface ProductPage extends Page<ProductResponseDTO> {}
}
