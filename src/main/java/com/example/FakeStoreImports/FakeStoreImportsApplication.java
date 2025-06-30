package com.example.FakeStoreImports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FakeStoreImportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeStoreImportsApplication.class, args);
    }

}
