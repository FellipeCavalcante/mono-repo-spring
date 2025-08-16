package com.dev.fellipe.user_service.service;

import com.dev.fellipe.exception.EmailAlreadyExistsException;
import com.dev.fellipe.exception.NotFoundException;
import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    //    @Transactional(rollbackFor = Exception.class)
    public User save(User user) {
        assertEmailDosNotExists(user.getEmail());
        return repository.save(user);
    }

    public void delete(Long id) {
        var user = findById(id);
        repository.delete(user);
    }

    public void update(User userToUpdate) {
        assertEmailDosNotExists(userToUpdate.getEmail(), userToUpdate.getId());
        var savedUser = findById(userToUpdate.getId());

        userToUpdate.setRoles(savedUser.getRoles());

        if (userToUpdate.getPassword() == null) userToUpdate.setPassword(savedUser.getPassword());

        repository.save(userToUpdate);
    }

    public void assertUserExist(Long id) {
        findById(id);
    }

    public void assertEmailDosNotExists(String email) {
        repository.findByEmail(email)
                .ifPresent(this::throwEmailExistsException);
    }

    public void assertEmailDosNotExists(String email, Long id) {
        repository.findByEmailAndIdNot(email, id)
                .ifPresent(this::throwEmailExistsException);
    }

    private void throwEmailExistsException(User user) {
        throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(user.getEmail()));
    }
}
