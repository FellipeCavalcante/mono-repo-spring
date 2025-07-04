package com.dev.fellipe.user_service.service;

import com.dev.fellipe.user_service.commons.ProfileUtils;
import com.dev.fellipe.user_service.domain.Profile;
import com.dev.fellipe.user_service.repository.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;

    @Mock
    private ProfileRepository repository;
    private List<Profile> profileList;

    @InjectMocks
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("findAll returns a lost with all profiles")
    @Order(1)
    void findAll_ReturnAllProfiles_WhenArgumentSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var profiles = service.findAll();
        Assertions.assertThat(profiles).isNotNull().hasSameElementsAs(profileList);
    }


    @Test
    @DisplayName("Save creates a profile")
    @Order(2)
    void save_CreatesProfile_WhenSuccessful() {
        var profileToSave = profileUtils.newProfileToSave();
        var profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(profileToSave)).thenReturn(profileSaved);

        var savedProfile = service.save(profileToSave);

        Assertions.assertThat(savedProfile).isEqualTo(profileSaved).hasNoNullFieldsOrProperties();
    }
}