package com.dev.fellipe.user_service.commons;

import com.dev.fellipe.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public List<User> newUserList() {
        var fellipe = User.builder().
                id(1L)
                .firstName("Fellipe")
                .lastName("Azevedo")
                .email("fellipe@azevedo.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$iq46TcNKxkLjsoyvceoJieq8qKwGfQboSmBXkiThewVMF0PQCvPVm")
                .build();
        var john = User.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$iq46TcNKxkLjsoyvceoJieq8qKwGfQboSmBXkiThewVMF0PQCvPVm")
                .build();
        var joao = User.builder()
                .id(3L)
                .firstName("João")
                .lastName("Silva")
                .email("joao@silva.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$iq46TcNKxkLjsoyvceoJieq8qKwGfQboSmBXkiThewVMF0PQCvPVm")
                .build();

        return new ArrayList<>(List.of(fellipe, john, joao));
    }

    public User newUserToSave() {
        return User.builder()
                .firstName("Manu")
                .lastName("Reginato")
                .email("manu@reginato.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$iq46TcNKxkLjsoyvceoJieq8qKwGfQboSmBXkiThewVMF0PQCvPVm")
                .build();
    }

    public User newUserSaved() {
        return User.builder()
                .id(99L)
                .firstName("Manu")
                .lastName("Reginato")
                .email("manu@reginato.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$iq46TcNKxkLjsoyvceoJieq8qKwGfQboSmBXkiThewVMF0PQCvPVm")
                .build();
    }
}
