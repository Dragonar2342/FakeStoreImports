package com.example.FakeStoreImports.controller;

import com.example.FakeStoreImports.dto.ProductRequestDTO;
import com.example.FakeStoreImports.dto.ProductResponseDTO;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.exception.ResourceNotFoundException;
import com.example.FakeStoreImports.service.ProductService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Tag(name = "Товары (View)", description = "HTML интерфейс для работы с товарами")
public class ProductViewController {

    private final ProductService productService;

    @GetMapping("/")
    @Operation(summary = "Главная страница", description = "Перенаправляет на страницу товаров")
    @Hidden
    public String viewHomePage(Model model) {
        return "redirect:/products";
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Список товаров", description = "Отображает HTML страницу со списком товаров")
    @ApiResponse(responseCode = "200", description = "HTML страница со списком товаров",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public String viewProductsPage(
            @Parameter(description = "Номер страницы", example = "1")
            @RequestParam("page") Optional<Integer> page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam("size") Optional<Integer> size,
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<ProductResponseDTO> productPage = productService.getAllProducts(
                PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("productPage", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "products";
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Детали товара", description = "Отображает HTML страницу с деталями товара")
    @ApiResponse(responseCode = "200", description = "HTML страница с деталями товара",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    public String viewProductDetails(@Parameter(description = "ID товара", example = "1", required = true)
                                         @PathVariable Long id, Model model) {
        ProductResponseDTO product = ProductResponseDTO.fromEntity(productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id)));
        model.addAttribute("product", product);
        return "product-details";
    }

    @GetMapping("/products/import")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Страница импорта", description = "Отображает HTML страницу для импорта товаров")
    @ApiResponse(responseCode = "200", description = "HTML страница импорта",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public String showImportPage(Model model) {
        return "import-products";
    }

    @PostMapping("/products/import")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Импорт товаров", description = "Выполняет импорт товаров из внешнего API")
    @ApiResponse(responseCode = "302", description = "Перенаправление на страницу импорта с сообщением")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public String importProducts(@Parameter(description = "Атрибуты для перенаправления") RedirectAttributes redirectAttributes) {
        productService.importProductsFromExternalApi();
        redirectAttributes.addFlashAttribute("message", "Products imported successfully!");
        return "redirect:/products/import";
    }

    @GetMapping("/products/new")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Форма создания", description = "Отображает HTML форму для создания нового товара")
    @ApiResponse(responseCode = "200", description = "HTML форма создания товара",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public String showCreateForm(Model model) {
        model.addAttribute("productRequest", new ProductRequestDTO());
        return "create-product";
    }

    @GetMapping("/products/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.getAllCategories());
        return "edit-product";
    }

    @PostMapping("/products/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Форма редактирования", description = "Отображает HTML форму для редактирования товара")
    @ApiResponse(responseCode = "200", description = "HTML форма редактирования товара",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    public String deleteProduct(@Parameter(description = "ID товара", example = "1", required = true)
            @PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        return "redirect:/products";
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Список категорий", description = "Отображает HTML страницу со списком категорий")
    @ApiResponse(responseCode = "200", description = "HTML страница со списком категорий",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    public String viewCategories(Model model) {
        model.addAttribute("categories", productService.getAllCategories());
        return "categories";
    }

    @GetMapping("/categories/{name}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Товары по категории", description = "Отображает HTML страницу с товарами категории")
    @ApiResponse(responseCode = "200", description = "HTML страница с товарами категории",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    public String viewProductsByCategory(
            @Parameter(description = "Название категории", example = "electronics", required = true)
            @PathVariable String name,
            @Parameter(description = "Номер страницы", example = "1")
            @RequestParam("page") Optional<Integer> page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam("size") Optional<Integer> size,
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<ProductResponseDTO> productPage = productService.getProductsByCategory(
                name, PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("productPage", productPage);
        model.addAttribute("categoryName", name);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "category-products";
    }
}