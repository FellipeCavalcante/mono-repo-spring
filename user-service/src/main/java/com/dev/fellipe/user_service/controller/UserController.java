package com.dev.fellipe.user_service.controller;

import com.dev.fellipe.user_service.mapper.UserMapper;
import com.dev.fellipe.user_service.request.UserPostRequest;
import com.dev.fellipe.user_service.request.UserPutRequest;
import com.dev.fellipe.user_service.response.UserGetResponse;
import com.dev.fellipe.user_service.response.UserPostResponse;
import com.dev.fellipe.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserMapper mapper;
    private final UserService service;


    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        log.debug("Request received to list all users, param first name '{}'", firstName);

        var users = service.findAll(firstName);

        var userGetResponses = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> getUser(@PathVariable Long id) {
        log.debug("Request to find user by id: {}", id);

        var user = service.findById(id);
        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> create(@RequestBody @Valid UserPostRequest request) {
        log.debug("Request to create user: {}", request);

        var user = mapper.toUser(request);

        var userSaved = service.save(user);

        var userPostResponse = mapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete user by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {
        var userToUpdate = mapper.toUser(request);
        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

}


