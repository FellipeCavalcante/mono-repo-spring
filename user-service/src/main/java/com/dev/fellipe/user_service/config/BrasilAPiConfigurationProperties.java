package com.dev.fellipe.user_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "brasil-api")
public record BrasilAPiConfigurationProperties(
        String baseUrl,
        String cepUri
) {
}
