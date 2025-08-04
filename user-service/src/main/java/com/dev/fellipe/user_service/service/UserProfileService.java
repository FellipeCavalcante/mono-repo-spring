package com.dev.fellipe.user_service.service;

import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.domain.UserProfile;
import com.dev.fellipe.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.findAll();
    }

    public List<User> findAllUsersByProfileId(Long id) {
        return repository.findAllUsersByProfileId(id);
    }
}
