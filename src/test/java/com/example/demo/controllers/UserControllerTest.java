package com.example.demo.controllers;

import com.example.demo.domain.*;
import com.example.demo.infra.security.TokenService;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository repository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(repository);
        userController.setTokenService(tokenService);
    }

    @Test
    void testGetAllUsers() {
        when(repository.findAll()).thenReturn(List.of(new User("1", "test@example.com", "Test User", UserRole.ADMIN)));

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetUserByID() {
        String userId = "1";
        User user = new User(userId, "test@example.com", "Test User", UserRole.ADMIN);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserByID(userId);

        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UserResponseDTO);
    }

    @Test
    void testPutUser() {
        String userId = "1";
        UserRequestDTO userRequest = new UserRequestDTO("new@example.com", "New User");
        User existingUser = new User(userId, "old@example.com", "Old User", UserRole.ADMIN);
        when(repository.existsById(userId)).thenReturn(true);
        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(tokenService.generateToken(any(User.class))).thenReturn("mockToken");

        ResponseEntity<?> response = userController.putUser(userId, userRequest);

        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LoginResponseDTO);
    }

    @Test
    void testDeleteUser() {
        String userId = "1";
        doNothing().when(repository).deleteById(userId);

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertEquals("Usuário deletado com sucesso.", response.getBody());
    }
}
