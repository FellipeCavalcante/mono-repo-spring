package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.commons.FIleUtis;
import com.dev.fellipe.user_service.commons.ProfileUtils;
import com.dev.fellipe.user_service.config.IntegrationTestBasicConfig;
import com.dev.fellipe.user_service.config.TestRestTemplateConfig;
import com.dev.fellipe.user_service.response.ProfileGetResponse;
import com.dev.fellipe.user_service.response.ProfilePostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRestTemplateConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileControllerIT extends IntegrationTestBasicConfig {
    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ProfileUtils profileUtils;
    @Autowired
    private FIleUtis fileUtils;

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles successful")
    @Sql(value = "/sql/profile/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/profile/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {};

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotEmpty().doesNotContainNull();

        responseEntity
                .getBody()
                .forEach(profileGetResponse -> Assertions.assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET v1/profiles returns empty list when nothing is not found")
    @Order(2)
    void findByName_ReturnEmptyList_WhenNothingIsFound() throws Exception {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {};

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("POST v1/profiles creates a profile")
    @Order(3)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var profileEntity = buildHttpEntity(request);

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, profileEntity, ProfilePostResponse.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        var profileJson = new HttpEntity<>(request, httpHeaders);
        return profileJson;
    }
}
