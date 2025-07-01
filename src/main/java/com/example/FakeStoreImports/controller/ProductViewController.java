package com.example.FakeStoreImports.controller;

import com.example.FakeStoreImports.dto.ProductRequestDTO;
import com.example.FakeStoreImports.dto.ProductResponseDTO;
import com.example.FakeStoreImports.entity.Product;
import com.example.FakeStoreImports.exception.ResourceNotFoundException;
import com.example.FakeStoreImports.service.ProductService;
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
public class ProductViewController {

    private final ProductService productService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return "redirect:/products";
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('USER')")
    public String viewProductsPage(
            @RequestParam("page") Optional<Integer> page,
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
    public String viewProductDetails(@PathVariable Long id, Model model) {
        ProductResponseDTO product = ProductResponseDTO.fromEntity(productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id)));
        model.addAttribute("product", product);
        return "product-details";
    }

    @GetMapping("/products/import")
    @PreAuthorize("hasRole('USER')")
    public String showImportPage(Model model) {
        return "import-products";
    }

    @PostMapping("/products/import")
    @PreAuthorize("hasRole('USER')")
    public String importProducts(RedirectAttributes redirectAttributes) {
        productService.importProductsFromExternalApi();
        redirectAttributes.addFlashAttribute("message", "Products imported successfully!");
        return "redirect:/products/import";
    }

    @GetMapping("/products/new")
    @PreAuthorize("hasRole('ADMIN')")
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
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        return "redirect:/products";
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('USER')")
    public String viewCategories(Model model) {
        model.addAttribute("categories", productService.getAllCategories());
        return "categories";
    }

    @GetMapping("/categories/{name}")
    @PreAuthorize("hasRole('USER')")
    public String viewProductsByCategory(
            @PathVariable String name,
            @RequestParam("page") Optional<Integer> page,
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