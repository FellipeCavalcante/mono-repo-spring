package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.commons.FIleUtis;
import com.dev.fellipe.user_service.commons.ProfileUtils;
import com.dev.fellipe.user_service.domain.Profile;
import com.dev.fellipe.user_service.repository.ProfileRepository;
import com.dev.fellipe.user_service.repository.UserProfileRepository;
import com.dev.fellipe.user_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.dev.fellipe")
@WithMockUser
class ProfileControllerTest {
    private static final String URL = "/v1/profiles";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileRepository repository;

    @MockBean
    private UserRepository userRepository;

    private List<Profile> profilesList;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FIleUtis fIleUtis;

    @Autowired
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        profilesList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles successful")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profilesList);
        var response = fIleUtis.readResourceFile("profile/get-profiles-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles returns empty list when nothing is not found")
    @Order(2)
    void findByName_ReturnEmptyList_WhenNothingIsFound() throws Exception {
        var response = fIleUtis.readResourceFile("profile/get-profiles-empty-list-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/profiles creates a profile")
    @Order(8)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = fIleUtis.readResourceFile("profile/post-request-profile-200.json");
        var response = fIleUtis.readResourceFile("profile/post-response-profile-201.json");

        var profileToSave = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileToSave);

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

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles returns bad request when fields are invalid")
    @Order(9)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileNames, List<String> errors) throws Exception {
        var request = fIleUtis.readResourceFile("profile/%s".formatted(fileNames));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors.toArray(new String[0]));
    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-profile-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static Stream<Arguments> putProfileBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-profile-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-profile-blank-fields-400.json", allRequiredErrors));
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";
        var descriptionRequiredError = "The field 'description' is required";

        return new ArrayList<>(List.of(nameRequiredError, descriptionRequiredError));
    }

}