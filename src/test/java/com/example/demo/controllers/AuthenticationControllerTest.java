package com.example.demo.controllers;

import com.example.demo.domain.AuthenticationDTO;
import com.example.demo.domain.RegisterDTO;
import com.example.demo.domain.User;
import com.example.demo.infra.security.TokenService;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.TestConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthenticationController.class)
@Import(AuthenticationControllerTest.TestSecurityConfig.class)
class AuthenticationControllerTest {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(csrf -> csrf.disable()).build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_Success() throws Exception {
        AuthenticationDTO loginData = new AuthenticationDTO("user@example.com", "password123");

        User mockUser = new User("user@example.com", "Test User", "encodedPassword", null);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(mockUser, loginData.password());

        when(authenticationManager.authenticate(Mockito.any())).thenReturn(authToken);
        when(tokenService.generateToken(Mockito.any(User.class))).thenReturn("mockToken");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"token\":\"mockToken\"}"));
    }


    @Test
    void register_Success() throws Exception {
        RegisterDTO registerData = new RegisterDTO("newuser@example.com", "New User", "password123", null);
        when(userRepository.findByLogin(registerData.login())).thenReturn(null);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerData)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário cadastrado com sucesso"));
    }

    @Test
    void login_InvalidEmail() throws Exception {
        AuthenticationDTO loginData = new AuthenticationDTO("invalid-email", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro: Email invalido."));
    }
}
