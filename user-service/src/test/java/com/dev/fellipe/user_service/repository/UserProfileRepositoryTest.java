package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.commons.UserUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserUtils.class)
@TestMethodOrder(MethodOrderer.class)
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository repository;

    @Test
    @DisplayName("findAll returns a list with all users by profile id")
    @Order(1)
    @Sql("/sql/init_user_profile_2_users_1_profile.sql")
    void findAllUsersByProfileId_ReturnAllUsersByProfileId_WhenSuccessful() {
        var profileId = 1L;
        var users = repository.findAllUsersByProfileId(profileId);

        org.assertj.core.api.Assertions.assertThat(users).isNotEmpty()
                .hasSize(2)
                .doesNotContainNull();

        users.forEach(user -> org.assertj.core.api.Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }
}