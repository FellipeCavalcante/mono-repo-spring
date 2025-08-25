package com.dev.fellipe.user_service.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static com.dev.fellipe.user_service.commons.Constants.*;

@TestConfiguration
@Lazy
public class RestAssuredConfig {
    @LocalServerPort
    int port;

    @Bean(name = "requestSpecificationRegularUser")
    public RequestSpecification requestSpecificationRegularUser() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                .auth().preemptive().basic(REGULAR_USERNAME, REGULAR_PASSWORD);
    }

    @Bean(name = "requestSpecificationAdminUser")
    public RequestSpecification requestSpecificationAdminUser() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                .auth().preemptive().basic(ADMIN_USERNAME, REGULAR_PASSWORD);
    }
}
