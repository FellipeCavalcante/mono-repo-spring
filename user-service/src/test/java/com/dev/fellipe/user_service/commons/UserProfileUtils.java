package com.dev.fellipe.user_service.commons;

import com.dev.fellipe.user_service.domain.Profile;
import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUtils {
    private final UserUtils userUtils;
    private final ProfileUtils profileUtils;

    public List<UserProfile> newProfileList() {
        var regularUserProfile = newUSerProfileSaved();

        return new ArrayList<>(List.of(regularUserProfile));
    }

    public UserProfile newUSerProfileToSave() {
        return UserProfile.builder()
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }


    public UserProfile newUSerProfileSaved() {
        return UserProfile.builder()
                .id(1L)
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }
}
