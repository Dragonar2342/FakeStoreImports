package com.example.FakeStoreImports.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSynchronizationService {

    private final ProductService productService;

    @Scheduled(fixedRate = 30 * 60 * 1000) // 30 минут в миллисекундах
    public void synchronizeProducts() {
        log.info("Starting products synchronization from external API...");
        try {
            productService.importProductsFromExternalApi();
            log.info("Products synchronization completed successfully");
        } catch (Exception e) {
            log.error("Error during products synchronization", e);
        }
    }
}
