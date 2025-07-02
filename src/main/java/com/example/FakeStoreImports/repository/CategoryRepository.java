package com.example.FakeStoreImports.repository;

import com.example.FakeStoreImports.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Operation(summary = "Найти категорию по имени",
            description = "Возвращает категорию с указанным именем, если она существует")
    Optional<Category> findByName(
            @Parameter(description = "Название категории", example = "electronics", required = true)
            String name);

    @Operation(summary = "Получить все уникальные имена категорий",
            description = "Возвращает список всех уникальных имен категорий")
    @Query("SELECT DISTINCT c.name FROM Category c")
    List<String> findAllUniqueCategoryNames();
}