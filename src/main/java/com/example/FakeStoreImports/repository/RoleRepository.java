package com.example.FakeStoreImports.repository;

import com.example.FakeStoreImports.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Operation(summary = "Найти роль по имени",
            description = "Возвращает роль с указанным именем, если она существует")
    Optional<Role> findByName(
            @Parameter(description = "Название роли", example = "ROLE_USER", required = true)
            String name);
}