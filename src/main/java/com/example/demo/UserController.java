package com.example.demo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequestMapping("/api")
public class UserController {

	public JSONObject users = new JSONObject();

	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}

	public UserController() {
		readJson();
	}

	public void writeJson() {
		try (FileWriter fw = new FileWriter("Users.json")){
			fw.write(this.users.toString());
			fw.close();
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
	
	@PostMapping("/login")
    public String login(@RequestBody User user) {
        if (this.users.has(Long.toString(user.getId()))){
			return "Erro: usuário já cadastrado.";
		}
		JSONObject user_json = new JSONObject();
		user_json.put("nome", user.getNome());
		user_json.put("login", user.getLogin());
		this.users.put(Long.toString(user.getId()), user_json);
		writeJson();
		return "Usuário cadastrado com sucesso.";
    }


	@PostMapping("/UserService/users")
	public String postUser(@RequestBody User user){
		if (this.users.has(Long.toString(user.getId()))){
			return "Erro: usuário já cadastrado.";
		}
		JSONObject user_json = new JSONObject();
		user_json.put("nome", user.getNome());
		user_json.put("login", user.getLogin());
		this.users.put(Long.toString(user.getId()), user_json);
		writeJson();
		return "Usuário cadastrado com sucesso.";
	}

	@GetMapping("/UserService/users")
	public String getUsers() {
		return this.users.toString();			
	}

	@GetMapping("/UserService/users/{id}")
	public String getUserByID(@PathVariable("id") Long id) {
		JSONObject user = this.users.getJSONObject(Long.toString(id));
		return user.toString();
	}

	@PutMapping("/UserService/users/{id}")
	public String putUser(@PathVariable("id") Long id, @RequestBody User user) {
		JSONObject user_json = new JSONObject();
		user_json.put("nome", user.getNome());
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
