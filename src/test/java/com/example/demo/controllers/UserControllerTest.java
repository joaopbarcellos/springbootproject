package com.example.demo.controllers;

import com.example.demo.domain.*;
import com.example.demo.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserControllerTest {
    @Mock
    UserRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve retonar todos os usuários cadastrados")
    void getAllUsersCasa1() {
        List<UserResponseDTO> userList = this.repository.findAll().stream().map(UserResponseDTO::new).toList();
        assertThat(userList).isNotNull();
    }

    @Test
    @DisplayName("Não achou nenhum usuário quando não há usuários")
    void getAllUsersCasa2() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<UserResponseDTO> userList = this.repository.findAll().stream().map(UserResponseDTO::new).toList();
        assertThat(userList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar um usuário com Id informado")
    void getUserByIDCase1(){
        String Id = "1111";
        UserDTO data = new UserDTO(Id,"login@gmail.com", "name", "12345", UserRole.USER);
        User newUser = new User(data);

        when(this.repository.findById(Id)).thenReturn(Optional.of(newUser));
        Optional<User> user = this.repository.findById(Id);
        assertThat(user.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar erro quando Id não for informado")
    void getUserByIDCase2(){
        String Id = "";
        when(this.repository.findById(Id)).thenThrow(new IllegalArgumentException("Id não pode ser vazio"));
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            this.repository.findById(Id);
        });
        assertThat(exception.getMessage()).isEqualTo("Id não pode ser vazio");
    }

    @Test
    @DisplayName("Deve retornar erro quando usuário não for encontrado")
    void getUserByIDCase3(){
        String Id = "13";
        when(this.repository.findById(Id)).thenReturn(Optional.empty());

        Optional<User> user = this.repository.findById(Id);
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve atualizar os dados do usuário")
    void putUser() {
        String Id = "1111";
        User notUpdatedUser = new User(new UserDTO(Id,"login@gmail.com", "name", "12345", UserRole.USER));
        when(this.repository.findById(Id)).thenReturn(Optional.of(notUpdatedUser));
        UserRequestDTO request = new UserRequestDTO("login2@gmail.com", "updatedname");

        User updatedUser = this.repository.findById(Id).orElseThrow();
        updatedUser.setLogin(request.login());
        updatedUser.setName(request.name());

        verify(this.repository, times(1)).save(notUpdatedUser);
    }

    @Test
    @DisplayName("Deve apagar o usuário")
    void deleteUser() {
        String Id = "1111";
        User user = new User(new UserDTO(Id,"login@gmail.com", "name", "12345", UserRole.USER));
        when(this.repository.findById(Id)).thenReturn(Optional.of(user));
        verify(repository, times(1)).delete(user);

    }

}