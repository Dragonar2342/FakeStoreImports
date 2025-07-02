package com.example.FakeStoreImports.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для регистрации нового пользователя")
public class RegistrationDTO {
    @Schema(description = "Имя пользователя",
            example = "user123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 50)
    private String username;

    @Schema(description = "Пароль",
            example = "securePassword123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6,
            maxLength = 100)
    private String password;
}