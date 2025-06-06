package com.dev.fellipe.user_service.commons;

import com.dev.fellipe.user_service.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileUtils {
    public List<Profile> newProfileList() {
        var admin = Profile.builder().id(1L).name("Administrator").description("Admins everythinh").build();
        var manager = Profile.builder().id(2L).name("Manager").description("Managers users").build();

        return new ArrayList<>(List.of(admin, manager));
    }

    public Profile newProfileToSave() {
        return Profile.builder()
                .name("Regular user")
                .description("Regular user with regular permissions")
                .build();
    }

    public Profile newProfileSaved() {
        return Profile.builder()
                .id(99L)
                .name("Regular user")
                .description("Regular user with regular permissions")
                .build();
    }
}
