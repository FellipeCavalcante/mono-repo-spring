package com.dev.fellipe.user_service.service;

import com.dev.fellipe.exception.NotFoundException;
import com.dev.fellipe.user_service.commons.CepUtils;
import com.dev.fellipe.user_service.config.BrasilAPiConfigurationProperties;
import com.dev.fellipe.user_service.config.RestClientConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

@RestClientTest({BrasilApiService.class,
        RestClientConfiguration.class,
        BrasilAPiConfigurationProperties.class,
        ObjectMapper.class,
        CepUtils.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrasilApiServiceTest {

    @Autowired
    private BrasilApiService service;
    @Autowired
    @Qualifier("brasilApiClient")
    private RestClient.Builder brasilApiClientBuilder;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private BrasilAPiConfigurationProperties properties;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CepUtils cepUtils;

    @AfterEach
    void reset() {
        server.reset();
    }

    @Order(1)
    @Test
    @DisplayName("findCep return CepGetResponse when successful")
    void findCep_ReturnsCepGetResponse_WhenSuccessful() throws JsonProcessingException {
        server = MockRestServiceServer.bindTo(brasilApiClientBuilder).build();

        var cep = "00000000";

        var cepGetResponse = cepUtils.newGetCepResponse();
        var jsonResponse = mapper.writeValueAsString(cepGetResponse);

        var requestTo = MockRestRequestMatchers.requestToUriTemplate(properties.baseUrl() + properties.cepUri(), cep);
        var withSuccess = MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON);

        server.expect(requestTo).andRespond(withSuccess);

        Assertions.assertThat(service.findCep(cep))
                .isNotNull()
                .isEqualTo(cepGetResponse);
    }

    @Order(2)
    @Test
    @DisplayName("findCep return CepErrorResponse when unsuccessful")
    void findCep_ReturnsCepErrorResponse_WhenSuccessful() throws JsonProcessingException {
        server = MockRestServiceServer.bindTo(brasilApiClientBuilder).build();

        var cep = "40400000";

        var cepErrorResponse = cepUtils.newCepErrorResponse();
        var jsonResponse = mapper.writeValueAsString(cepErrorResponse);
        var expectedErrorMessage = """
                    404 NOT_FOUND "CepErrorResponse[name=CepPromiseError, message=Todos os serviços de CEP retornaram erro., type=service_error, errors=[CepInnerErrorResponse[name=ServiceError, message=CEP INVÁLIDO, service=correios]]]"
                """.trim();

        var requestTo = MockRestRequestMatchers.requestToUriTemplate(properties.baseUrl() + properties.cepUri(), cep);
        var withSuccess = MockRestResponseCreators.withResourceNotFound().body(jsonResponse);

        server.expect(requestTo).andRespond(withSuccess);

        Assertions.assertThatException()
                .isThrownBy(() -> service.findCep(cep))
                .withMessage(expectedErrorMessage)
                .isInstanceOf(NotFoundException.class);
    }
}
