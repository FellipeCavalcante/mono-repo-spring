package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.commons.UserUtils;
import com.dev.fellipe.user_service.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;

    @Mock
    private UserData userData;
    private List<User> userList;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a lost with all users")
    void findAll_ReturnAllUser_WhenSuccesful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findAll();
        Assertions.assertThat(users).hasSize(users.size());
    }


    @Test
    @DisplayName("findById returns a user with given id")
    void findById_ReturnUserById_WhenSuccesful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var expectedUser = userList.getFirst();
        var users = repository.findById(expectedUser.getId());
        Assertions.assertThat(users).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    void findByName_ReturnEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findByName(null);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    void findByName_ReturnsUserList_WhenNameExists() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var expectedUser = userList.getFirst();
        var users = repository.findByName(expectedUser.getFirstName());
        Assertions.assertThat(users).contains(expectedUser);
    }

    @Test
    @DisplayName("Save creates a user")
    void save_CreatesProducer_WhenSuccesful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToSave = userUtils.newUserToSave();

        var user = repository.save(userToSave);
        Assertions.assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();

        var userSavedOptional = repository.findById(userToSave.getId());
        Assertions.assertThat(userSavedOptional).isPresent().contains(userToSave);
    }

    @Test
    @DisplayName("delete remove a user")
    void delete_RemovesUser_WhenSuccesful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToRemove = userList.getFirst();
        repository.delete(userToRemove);
        Assertions.assertThat(this.userList).doesNotContain(userToRemove);
    }

    @Test
    @DisplayName("update uptade a user")
    void update_RemovesUser_WhenSuccesful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("test");
        repository.update(userToUpdate);
        Assertions.assertThat(this.userList).contains(userToUpdate);

        var userUpdatedOptional = repository.findById(userToUpdate.getId());
        Assertions.assertThat(userUpdatedOptional).isPresent();
        Assertions.assertThat(userUpdatedOptional.get().getFirstName()).isEqualTo("test");
    }

}