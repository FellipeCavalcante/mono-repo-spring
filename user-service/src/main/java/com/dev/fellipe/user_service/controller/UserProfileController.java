package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.mapper.UserProfileMapper;
import com.dev.fellipe.user_service.response.UserProfileGetResponse;
import com.dev.fellipe.user_service.response.UserProfileUserGetResponse;
import com.dev.fellipe.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@Slf4j
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        log.debug("Request received to list all user profiles");

        var userProfiles = service.findAll();
        var userProfileGetResponses = mapper.toUserProfileGetResponse(userProfiles);

        return ResponseEntity.ok(userProfileGetResponses);
    }

    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAll(@PathVariable Long id) {
        log.debug("Request received to list all users by profile id '{}'", id);

        var users = service.findAllUsersByProfileId(id);

        var userProfileUserGetResponseList = mapper.toUserProfileUserGetResponseList(users);

        return ResponseEntity.ok(userProfileUserGetResponseList);
    }
}
