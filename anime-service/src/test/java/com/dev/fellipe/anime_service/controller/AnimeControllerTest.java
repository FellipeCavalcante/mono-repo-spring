package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.commons.AnimeUtils;
import com.dev.fellipe.anime_service.commons.FIleUtis;
import com.dev.fellipe.anime_service.domain.Anime;
import com.dev.fellipe.anime_service.repository.AnimeData;
import com.dev.fellipe.anime_service.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.dev.fellipe")
class AnimeControllerTest {
    private static final String URL = "/v1/animes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeData animeData;

    @SpyBean
    private AnimeHardCodedRepository repository;
    private List<Anime> animesList;

    @Autowired
    private FIleUtis fIleUtis;

    @Autowired
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animesList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("GET v1/animes returns a list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fIleUtis.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=Ufotable returns list with found object when name exists")
    @Order(2)
    void findByName_ReturnsProducerList_WhenNameExists() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fIleUtis.readResourceFile("anime/get-anime-mashle-name-200.json");
        var name = "Mashle";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=x returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fIleUtis.readResourceFile("anime/get-anime-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 returns a animes with given id")
    @Order(4)
    void findById_ReturnProducersById_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fIleUtis.readResourceFile("anime/get-anime-by-id-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 throws NotFound 404 when anime is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fIleUtis.readResourceFile("anime/get-anime-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("POST v1/animes creates a anime")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = fIleUtis.readResourceFile("anime/post-request-anime-200.json");
        var response = fIleUtis.readResourceFile("anime/post-response-anime-201.json");

        var animeToSave = animeUtils.newAnimeToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

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
    @DisplayName("PUT v1/animes updates a anime")
    @Order(7)
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var request = fIleUtis.readResourceFile("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes throws NotFound when anime is not found")
    @Order(8)
    void update_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var request = fIleUtis.readResourceFile("anime/put-request-anime-404.json");
        var response = fIleUtis.readResourceFile("anime/put-anime-by-id-404.json");

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
    @DisplayName("DELETE v1/animes/1 remove a anime")
    @Order(9)
    void delete_RemoveProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var animeId = animesList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/animes/99 throws NotFound when anime is not found")
    @Order(10)
    void delete_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fIleUtis.readResourceFile("anime/delete-anime-by-id-404.json");
        var animeId = 99;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileNames, List<String> errors) throws Exception {
        var request = fIleUtis.readResourceFile("anime/%s".formatted(fileNames));

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

    private static Stream<Arguments> postAnimeBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-anime-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-anime-blank-fields-400.json", allRequiredErrors)
        );
    }

    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSource")
    @DisplayName("PUT v1/animes returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileNames, List<String> errors) throws Exception {
        var request = fIleUtis.readResourceFile("anime/%s".formatted(fileNames));
        var response = fIleUtis.readResourceFile("anime/delete-anime-by-id-404.json");

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

    private static Stream<Arguments> putAnimeBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-anime-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-anime-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";

        return new ArrayList<>(List.of(nameRequiredError));
    }
}