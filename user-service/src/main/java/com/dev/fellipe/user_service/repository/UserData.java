package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    private final List<User> users = new ArrayList<>();

    {
        var naruto = User.builder().id(1L).firstName("Naruto").lastName("Uzumaki").email("uzumaki@konoha.com").build();
        var gojo = User.builder().id(2L).firstName("Satoro").lastName("Gojo").email("gojo@jujutsu.com").build();
        var ippo = User.builder().id(3L).firstName("Ippo").lastName("makunouchi").email("makunouchi@kamogawa.com").build();
        users.addAll(List.of(naruto, gojo, ippo));
    }

    public List<User> getUsers() {
        return users;
    }

}
