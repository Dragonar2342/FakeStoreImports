package com.example.FakeStoreImports.repository;

import com.example.FakeStoreImports.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query("SELECT DISTINCT c.name FROM Category c")
    List<String> findAllUniqueCategoryNames();
}
