package com.dev.fellipe.user_service.service;

import com.dev.fellipe.user_service.config.BrasilAPiConfigurationProperties;
import com.dev.fellipe.user_service.response.CepGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class BrasilApiService {

    @Qualifier("brasilApiClient")
    private final RestClient.Builder brasilApiClient;

    private final BrasilAPiConfigurationProperties brasilAPiConfigurationProperties;

    public CepGetResponse findCep(String cep) {
        return brasilApiClient.build()
                .get()
                .uri(brasilAPiConfigurationProperties.cepUri(), cep)
                .retrieve()
                .body(CepGetResponse.class);
    }
}
