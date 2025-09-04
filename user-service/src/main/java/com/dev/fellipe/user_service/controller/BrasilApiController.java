package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.config.BrasilAPiConfigurationProperties;
import com.dev.fellipe.user_service.response.CepGetResponse;
import com.dev.fellipe.user_service.service.BrasilApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api/cep")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class BrasilApiController {

    private final BrasilAPiConfigurationProperties brasilAPiConfigurationProperties;
    private final BrasilApiService service;

    @GetMapping("/{cep}")
    public ResponseEntity<CepGetResponse> getCep(@PathVariable String cep) {
        log.info("Request received to find cep");

        var cepGetResponse = service.findCep(cep);

        return ResponseEntity.ok(cepGetResponse);
    }
}
