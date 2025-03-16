package com.dev.fellipe.user_service.service;

import com.dev.fellipe.user_service.commons.UserUtils;
import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.repository.UserHardCodedRepository;
import com.dev.fellipe.user_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserHardCodedRepository repository;
    @Mock
    private UserRepository userRepository;
    private List<User> userList;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a lost with all users when argument is null")
    @Order(1)
    void findAll_ReturnAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(userRepository.findAll()).thenReturn(userList);

        var users = service.findAll(null);
        org.assertj.core.api.Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns list with found object when name exists")
    @Order(2)
    void findByName_ReturnsUserList_WhenNameExists() {
        User user = userList.getFirst();
        var expectedUsersFound = singletonList(user);
        BDDMockito.when(repository.findByName(user.getFirstName())).thenReturn(expectedUsersFound);

        var usersFound = service.findAll(user.getFirstName());
        org.assertj.core.api.Assertions.assertThat(usersFound).containsAll(expectedUsersFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(emptyList());

        var users = service.findAll(name);
        org.assertj.core.api.Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a users with given id")
    @Order(4)
    void findById_ReturnUsersById_WhenSuccesful() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        var users = service.findById(expectedUser.getId());
        org.assertj.core.api.Assertions.assertThat(users).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());


        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.findById(expectedUser.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Save creates a user")
    @Order(6)
    void save_CreatesUser_WhenSuccesful() {
        var userToSave = userUtils.newUserToSave();
        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);

        var savedUser = service.save(userToSave);
        org.assertj.core.api.Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete remove a user")
    @Order(7)
    void delete_RemoveUser_WhenSuccesful() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

        BDDMockito.doNothing().when(repository).delete(userToDelete);
        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when user is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(9)
    void update_UpdatesUser_WhenSuccesful() {
        var userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("updatedName");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.doNothing().when(repository).update(userToUpdate);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToUpdate = userList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}