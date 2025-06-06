package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.mapper.ProfileMapper;
import com.dev.fellipe.user_service.request.ProfilePostRequest;
import com.dev.fellipe.user_service.response.ProfileGetResponse;
import com.dev.fellipe.user_service.response.ProfilePostResponse;
import com.dev.fellipe.user_service.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileMapper mapper;
    private final ProfileService service;


    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll() {
        log.debug("Request received to list all profiles '{}'");

        var profiles = service.findAll();

        var profileGetResponses = mapper.toProfileGetResponseList(profiles);

        return ResponseEntity.ok(profileGetResponses);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> create(@RequestBody @Valid ProfilePostRequest request) {
        log.debug("Request to create profile: {}", request);

        var profile = mapper.toProfile(request);

        var profileSaved = service.save(profile);

        var profilePostResponse = mapper.toProfilePostResponse(profileSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(profilePostResponse);
    }
}


