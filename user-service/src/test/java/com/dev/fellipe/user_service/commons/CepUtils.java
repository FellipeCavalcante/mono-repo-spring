package com.dev.fellipe.user_service.commons;

import com.dev.fellipe.user_service.response.CepGetResponse;
import org.springframework.stereotype.Component;

@Component
public class CepUtils {


    public CepGetResponse newGetCepResponse() {
        return CepGetResponse.builder()
                .cep("00000000")
                .city("São Paulo")
                .neighborhood("Vila Mariana")
                .street("Rua 123")
                .service("viacep")
                .build();
    }
}
