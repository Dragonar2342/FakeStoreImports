package com.example.FakeStoreImports.repository;

import com.example.FakeStoreImports.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Operation(summary = "Получить все товары",
            description = "Возвращает страницу со всеми товарами с поддержкой пагинации")
    Page<Product> findAll(
            @Parameter(description = "Параметры пагинации и сортировки")
            Pageable pageable);

    @Operation(summary = "Фильтр товаров по цене",
            description = "Возвращает товары в указанном диапазоне цен")
    @Query("SELECT p FROM Product p WHERE (:minPrice IS NULL OR p.price >= :minPrice) AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findByPriceBetween(
            @Parameter(description = "Минимальная цена", example = "10.0")
            @Param("minPrice") BigDecimal minPrice,
            @Parameter(description = "Максимальная цена", example = "100.0")
            @Param("maxPrice") BigDecimal maxPrice,
            @Parameter(description = "Параметры пагинации")
            Pageable pageable);

    @Operation(summary = "Найти товары по категории",
            description = "Возвращает товары, принадлежащие указанной категории")
    Page<Product> findByCategoryName(
            @Parameter(description = "Название категории", example = "electronics", required = true)
            String categoryName,
            @Parameter(description = "Параметры пагинации")
            Pageable pageable);

    @Operation(summary = "Получить все ID товаров",
            description = "Возвращает множество всех идентификаторов товаров")
    @Query("SELECT p.id FROM Product p")
    Set<Long> findAllIds();
}