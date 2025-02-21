package com.dev.fellipe.anime_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database")
public record ConnectionConfigurationProperties(String url, String username, String password) {
}
