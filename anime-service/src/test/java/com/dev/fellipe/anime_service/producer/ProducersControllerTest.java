package com.dev.fellipe.anime_service.producer;

import com.dev.fellipe.anime_service.commons.FIleUtis;
import com.dev.fellipe.anime_service.commons.ProducerUtils;
import com.dev.fellipe.anime_service.domain.Producer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(controllers = ProducersController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"com.dev.fellipe.anime_service.producer", "com.dev.fellipe.anime_service.commons", "com.dev.fellipe.exception"})
//@ActiveProfiles("test")
class ProducersControllerTest {
    private static final String URL = "/v1/producers";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerRepository repository;
    private List<Producer> producerList;

    @Autowired
    private FIleUtis fIleUtis;

    @Autowired
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("GET v1/producers returns list with found object when name exists")
    @Order(1)
    void findAll_ReturnAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);
        var response = fIleUtis.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns list with found object when name exists")
    @Order(2)
    void findByName_ReturnsProducerList_WhenNameExists() throws Exception {
        var response = fIleUtis.readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";
        var ufotable = producerList.stream().filter(producer -> producer.getName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(ufotable));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fIleUtis.readResourceFile("producer/get-producer-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 returns a producers with given id")
    @Order(4)
    void findById_ReturnProducersById_WhenSuccessful() throws Exception {
        var response = fIleUtis.readResourceFile("producer/get-producer-by-id-200.json");
        var id = 1L;
        var foundProducer = producerList.stream().filter(producer -> producer.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/99 throws NotFound 404 when producer is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        var response = fIleUtis.readResourceFile("producer/get-producer-by-id-404.json");

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = fIleUtis.readResourceFile("producer/post-request-producer-200.json");
        var response = fIleUtis.readResourceFile("producer/post-response-producer-201.json");

        var producerToSave = producerUtils.newProducerToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-key", "v1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/producers updates a producer")
    @Order(7)
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        var request = fIleUtis.readResourceFile("producer/put-request-producer-200.json");
        var id = 1L;
        var foundProducer = producerList.stream().filter(producer -> producer.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers throws NotFound when producer is not found")
    @Order(8)
    void update_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        var request = fIleUtis.readResourceFile("producer/put-request-producer-404.json");
        var response = fIleUtis.readResourceFile("producer/put-producer-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("DELETE v1/producers/1 remove a producer")
    @Order(9)
    void delete_RemoveProducer_WhenSuccessful() throws Exception {
        var producerId = producerList.getFirst().getId();
        var foundProducer = producerList.stream().filter(producer -> producer.getId().equals(producerId)).findFirst();
        BDDMockito.when(repository.findById(producerId)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", producerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers/99 throws NotFound when producer is not found")
    @Order(10)
    void delete_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        var response = fIleUtis.readResourceFile("producer/delete-producer-by-id-404.json");
        var producerId = 99;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", producerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postProducerBadRequestSource")
    @DisplayName("POST v1/producers returns bad request when fields are invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileNames, List<String> errors) throws Exception {
        var request = fIleUtis.readResourceFile("producer/%s".formatted(fileNames));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        assertThat(resolvedException).isNotNull();

        assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> postProducerBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-producer-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-producer-blank-fields-400.json", allRequiredErrors)
        );
    }

    @ParameterizedTest
    @MethodSource("putProducerBadRequestSource")
    @DisplayName("PUT v1/producers returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileNames, List<String> errors) throws Exception {
        var request = fIleUtis.readResourceFile("producer/%s".formatted(fileNames));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        assertThat(resolvedException).isNotNull();

        assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> putProducerBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-producer-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";

        return new ArrayList<>(List.of(nameRequiredError));
    }
}