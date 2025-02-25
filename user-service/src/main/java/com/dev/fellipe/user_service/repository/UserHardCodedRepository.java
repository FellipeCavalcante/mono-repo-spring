package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHardCodedRepository {

    private final UserData userData;

    public Optional<User> findById(Long id) {
        return userData.getUsers().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public List<User> findAll() {
        return userData.getUsers();
    }

    public List<User> findByName(String name) {
        return userData.getUsers().stream().filter(user -> user.getFirstName().equalsIgnoreCase(name)).toList();
    }

    public User save(User user) {
        userData.getUsers().add(user);
        return user;
    }

    public void delete(User user) {
        userData.getUsers().remove(user);
    }

    public void update(User user) {
        delete(user);
        save(user);
    }
}
