package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.config.BrasilAPiConfigurationProperties;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api/cep")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class BrasilApiController {

    private final BrasilAPiConfigurationProperties brasilAPiConfigurationProperties;

    @GetMapping
    public ResponseEntity<Void> testApi() {
        log.info("Request received to find cep");
        log.info("base-url {}, cepUri {}", brasilAPiConfigurationProperties.baseUrl(), brasilAPiConfigurationProperties.cepUri());
        return ResponseEntity.noContent().build();
    }
}
