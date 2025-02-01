package com.example.demo.controllers;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRole;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {
    @Mock
    private UserRepository repository;
    @Test
    void login() {
    }

    @Test
    @DisplayName("Deve cadastrar o usuário.")
    void register() {
        User user = new User("login1", "user1", "123456", UserRole.USER);

        when(this.repository.findByLogin(user.getLogin())).thenReturn(user);

    }
}