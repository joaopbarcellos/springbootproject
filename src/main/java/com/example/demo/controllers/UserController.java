package com.example.demo.controllers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.example.demo.domain.User;
import com.example.demo.repositories.UserRepository;
import org.apache.coyote.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.UserResponseDTO;

@RestController
@RequestMapping("/api")
public class UserController {

	public JSONObject users = new JSONObject();

	@Autowired
	UserRepository repository;
//	public UserController() {
//		readJson();
//	}

	public void writeJson() {
		try (FileWriter fw = new FileWriter("Users.json")){
			fw.write(this.users.toString());
        } catch (IOException e) {
			System.err.println("Erro ao escrever no arquivo.");
		}
	}

	public final void readJson() {
		try (FileReader fr = new FileReader("Users.json")) {
			this.users = new JSONObject(new JSONTokener(fr));
		} catch (IOException e) {
			this.users = new JSONObject();
		}
	}

	@PostMapping("/UserService/users")
	public String postUser(@RequestBody User user){
		if (this.users.has(user.getId())){
			return "Erro: usuário já cadastrado.";
		}
		JSONObject user_json = new JSONObject();
		user_json.put("login", user.getLogin());
		this.users.put(user.getId(), user_json);
		writeJson();
		return "Usuário cadastrado com sucesso.";
	}

	@GetMapping("/UserService/users")
	public ResponseEntity getAllUsers() {
		List<UserResponseDTO> userList = this.repository.findAll().stream().map(UserResponseDTO::new).toList();

		return ResponseEntity.ok(userList);
	}

	@GetMapping("/UserService/users/{id}")
	public ResponseEntity getUserByID(@PathVariable("id") String id) {
		UserResponseDTO user = this.repository.findById(id).stream().map(UserResponseDTO::new).findFirst().get();
		return ResponseEntity.ok(user);
	}

	@PutMapping("/UserService/users/{id}")
	public String putUser(@PathVariable("id") Long id, @RequestBody User user) {
		JSONObject user_json = new JSONObject();
		user_json.put("login", user.getLogin());
		this.users.put(Long.toString(id), user_json);
		writeJson();
		return "Usuário atualizado com sucesso.";
	}

	@DeleteMapping("/UserService/users/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		this.users.remove(Long.toString(id));
		writeJson();
		return "Usuário removido com sucesso.";
	}
}
