package com.example.demo.controllers;

import java.util.List;

import com.example.demo.domain.LoginResponseDTO;
import com.example.demo.domain.User;
import com.example.demo.domain.UserRequestDTO;
import com.example.demo.infra.security.TokenService;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.UserResponseDTO;

import static com.example.demo.services.EmailValidator.isValidEmail;

@RestController
@RequestMapping("/api")
public class UserController {

	final
	UserRepository repository;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Setter
    @Autowired
	private TokenService tokenService;

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
	public ResponseEntity<?> putUser(@PathVariable("id") String id, @RequestBody @Valid UserRequestDTO body) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(404).body("Erro: Usuário não encontrado.");
		}

		User user = new User(body);
		if ((user.getLogin() == null || user.getLogin().trim().isEmpty()) &&
				(user.getName() == null || user.getName().trim().isEmpty())) {
			return ResponseEntity.badRequest().body("Erro: Login ou nome deve ser informado");
		}
		if (!isValidEmail(user.getLogin()) && user.getLogin() != null) {
			return ResponseEntity.badRequest().body("Erro: Email invalido.");
		}

		User existingUser = repository.findById(id).orElseThrow();

		if (user.getLogin() != null) {
			existingUser.setLogin(user.getLogin());
		}
		if (user.getName() != null) {
			existingUser.setName(user.getName());
		}

		repository.save(existingUser);
		var authentication = new UsernamePasswordAuthenticationToken(existingUser, null, existingUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// ✅ Generate a new token for the updated user
		var token = tokenService.generateToken(existingUser);
		return ResponseEntity.ok(new LoginResponseDTO(token));
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
