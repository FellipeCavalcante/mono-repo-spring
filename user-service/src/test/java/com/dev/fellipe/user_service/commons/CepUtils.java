package com.dev.fellipe.user_service.commons;

import com.dev.fellipe.user_service.response.CepErrorResponse;
import com.dev.fellipe.user_service.response.CepGetResponse;
import com.dev.fellipe.user_service.response.CepInnerErrorResponse;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public CepErrorResponse newCepErrorResponse() {
        var cepInnerErrorResponse = CepInnerErrorResponse.builder()
                .name("ServiceError")
                .message("CEP INVÁLIDO")
                .service("correios")
                .build();

        return CepErrorResponse.builder()
                .name("CepPromiseError")
                .message("Todos os serviços de CEP retornaram erro.")
                .type("service_error")
                .errors(List.of(cepInnerErrorResponse))
                .build();
    }


}
