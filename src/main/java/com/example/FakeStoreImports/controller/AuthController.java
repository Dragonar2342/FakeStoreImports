package com.example.FakeStoreImports.controller;

import com.example.FakeStoreImports.dto.RegistrationDTO;
import com.example.FakeStoreImports.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для регистрации и входа пользователей")
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    @Operation(summary = "Страница входа",
            description = "Возвращает HTML страницу для авторизации пользователей")
    @ApiResponse(responseCode = "200",
            description = "Страница входа успешно загружена",
            content = @Content(mediaType = "text/html"))
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    @Operation(summary = "Форма регистрации",
            description = "Возвращает HTML страницу с формой регистрации нового пользователя")
    @ApiResponse(responseCode = "200",
            description = "Страница регистрации успешно загружена",
            content = @Content(mediaType = "text/html"))
    public String showRegistrationForm(
            @Parameter(description = "Модель для передачи данных в представление", hidden = true)
            Model model) {
        model.addAttribute("user", new RegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя",
            description = "Обрабатывает данные регистрации и создает нового пользователя")
    @ApiResponse(responseCode = "302",
            description = "Перенаправление на страницу входа после успешной регистрации")
    public String registerUser(
            @Parameter(description = "Данные для регистрации", required = true)
            @ModelAttribute("user") RegistrationDTO registrationDTO) {
        userService.registerUser(registrationDTO);
        return "redirect:/login?success";
    }
}