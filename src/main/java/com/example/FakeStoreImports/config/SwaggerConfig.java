package com.example.FakeStoreImports.config;

import com.example.FakeStoreImports.dto.ProductResponseDTO;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FakeStoreImports API")
                        .version("1.0.0")
                        .description("API для импорта и управления товарами из FakeStore API")
                        .contact(new Contact()
                                .name("Поддержка")
                                .email("support@fakestoreimports.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Локальный сервер разработки"),
                        new Server().url("https://api.fakestoreimports.com").description("Продукционный сервер")
                ))
                .components(new Components()
                        .addSchemas("ProductPage", new Schema<Page<ProductResponseDTO>>()
                                .name("ProductPage")
                                .description("Страница с товарами")));
    }
}