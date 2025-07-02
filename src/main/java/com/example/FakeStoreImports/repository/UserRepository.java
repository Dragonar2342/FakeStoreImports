package com.example.FakeStoreImports.repository;

import com.example.FakeStoreImports.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Operation(summary = "Найти пользователя по имени",
            description = "Возвращает пользователя с указанным именем, если он существует")
    Optional<User> findByUsername(
            @Parameter(description = "Имя пользователя", example = "user123", required = true)
            String username);
}