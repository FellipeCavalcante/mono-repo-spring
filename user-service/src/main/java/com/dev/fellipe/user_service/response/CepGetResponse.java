package com.dev.fellipe.user_service.response;

public record CepGetResponse(
        String cep,
        String state,
        String city,
        String neighborhood,
        String street,
        String service
) {
}
