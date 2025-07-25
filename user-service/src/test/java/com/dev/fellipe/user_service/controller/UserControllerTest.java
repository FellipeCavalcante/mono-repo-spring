package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.commons.FIleUtis;
import com.dev.fellipe.user_service.commons.UserUtils;
import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.repository.ProfileRepository;
import com.dev.fellipe.user_service.repository.UserRepository;
import com.dev.fellipe.user_service.service.ProfileService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.dev.fellipe")
class UserControllerTest {
    private static final String URL = "/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private UserRepository repository;
    private List<User> usersList;

    @Autowired
    private FIleUtis fIleUtis;

    @Autowired
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        usersList = userUtils.newUserList();
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(usersList);
        var response = fIleUtis.readResourceFile("user/get-user-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?firstName=Fellipe returns list with found object when first name exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenFirstNameIsFound() throws Exception {
        var response = fIleUtis.readResourceFile("user/get-user-fellipe-name-200.json");
        var firstName = "Fellipe";
        var fellipe = usersList.stream().filter(user -> user.getFirstName().equals(firstName)).findFirst().orElse(null);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.singletonList(fellipe));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=x returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fIleUtis.readResourceFile("user/get-user-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 returns a users with given id")
    @Order(4)
    void findById_ReturnProducersById_WhenSuccessful() throws Exception {
        var response = fIleUtis.readResourceFile("user/get-user-by-id-200.json");
        var id = 1L;
        var foundUser = usersList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/99 throws NotFound 404 when user is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var response = fIleUtis.readResourceFile("user/get-user-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/users/99 throws NotFound when user is not found")
    @Order(6)
    void delete_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var response = fIleUtis.readResourceFile("user/delete-user-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("DELETE v1/users/1 removes an user")
    @Order(7)
    void delete_RemoveUser_WhenSuccessful() throws Exception {
        var id = usersList.getFirst().getId();
        var foundUser = usersList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("POST v1/users creates a user")
    @Order(8)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = fIleUtis.readResourceFile("user/post-request-user-200.json");
        var response = fIleUtis.readResourceFile("user/post-response-user-201.json");

        var userToSave = userUtils.newUserToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

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
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are invalid")
    @Order(9)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileNames, List<String> errors) throws Exception {
        var request = fIleUtis.readResourceFile("user/%s".formatted(fileNames));

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
                .contains(errors);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        var emailError = invalidEmailErrors();

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-user-blank-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailError)
        );
    }

    @Test
    @DisplayName("PUT v1/users updates a user")
    @Order(10)
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        var request = fIleUtis.readResourceFile("user/put-request-user-200.json");
        var id = 1L;
        var foundUser = usersList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users throws NotFound when user is not found")
    @Order(11)
    void update_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var request = fIleUtis.readResourceFile("user/put-request-user-404.json");
        var response = fIleUtis.readResourceFile("user/put-user-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileNames, List<String> errors) throws Exception {
        var request = fIleUtis.readResourceFile("user/%s".formatted(fileNames));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' cannot be null");
        var emailError = invalidEmailErrors();

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-blank-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailError)
        );
    }

    private static List<String> allRequiredErrors() {
        var firstNameRequiredError = "The field 'firstName' is required";
        var lastNameRequiredError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";

        return new ArrayList<>(List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError));
    }

    private static List<String> invalidEmailErrors() {
        var emailInvalidError = "The e-mail is not valid";

        return List.of(emailInvalidError);
    }

}