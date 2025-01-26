package com.example.demo.controllers;

import java.util.List;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRequestDTO;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.UserResponseDTO;

@RestController
@RequestMapping("/api")
public class UserController {

	final
	UserRepository repository;

	@Autowired
	public UserController(UserRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/UserService/users")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		List<UserResponseDTO> userList = repository.findAll().stream().map(UserResponseDTO::new).toList();

		return ResponseEntity.ok(userList);
	}

	@GetMapping("/UserService/users/{id}")
	public ResponseEntity<?> getUserByID(@PathVariable("id") String id) {
		if (id == null || id.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Erro: O ID fornecido é inválido.");
		}
		UserResponseDTO user = repository.findById(id).stream().map(UserResponseDTO::new).findFirst().get();
		return ResponseEntity.ok(user);
	}

	@PutMapping("/UserService/users/{id}")
	public ResponseEntity<String> putUser(@PathVariable("id") String id, @RequestBody @Valid UserRequestDTO body) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(404).body("Erro: Usuário não encontrado.");
		}

		User user = new User(body);
		if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Erro: Login deve ser informado");
		}

		if (user.getName() == null || user.getName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Erro: Nome deve ser informado");
		}

		User existingUser = repository.findById(id).orElseThrow();

		existingUser.setLogin(user.getLogin());
		existingUser.setName(user.getName());
		repository.save(existingUser);

		// Lembrar de fazer uma nova requisição de Login
		return ResponseEntity.ok("Usuário atualizado com sucesso.");
	}

	@DeleteMapping("/UserService/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
		if (id == null || id.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Erro: Id deve ser informado.");
		}

		repository.deleteById(id);
		return ResponseEntity.ok("Usuário deletado com sucesso.");
	}
}
