package com.dev.fellipe.user_service.service;

import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.exception.NotFoundException;
import com.dev.fellipe.user_service.repository.UserHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHardCodedRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        var user = findById(id);
        repository.delete(user);
    }

    public void update(User userToUpdate) {
        assertUserExist(userToUpdate.getId());
        repository.update(userToUpdate);
    }

    public void assertUserExist(Long id) {
        findById(id);
    }
}
