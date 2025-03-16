package com.dev.fellipe.user_service.service;

import com.dev.fellipe.exception.NotFoundException;
import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.repository.UserHardCodedRepository;
import com.dev.fellipe.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHardCodedRepository repository;
    private final UserRepository userRepository;

    public List<User> findAll(String name) {
        return name == null ? userRepository.findAll() : repository.findByName(name);
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
