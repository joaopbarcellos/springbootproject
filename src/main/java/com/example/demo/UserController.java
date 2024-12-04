package com.example.demo;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@SpringBootApplication
@RequestMapping("/api/UserService/users")
public class UserController {

	public JSONObject users = new JSONObject();

	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}

	public void writeJson() {
		try {
			FileWriter fw = new FileWriter("Users.json");
			fw.write(this.users.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JSONObject readJson() {
		try {
			FileReader fr = new FileReader("Users.json");
			JSONObject users = new JSONObject(new JSONTokener(fr));
			fr.close();
			return users;
		} catch (IOException e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}

	@PostMapping()
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

	@GetMapping()
	public String getUsers() {
		return this.users.toString();			
	}

	@GetMapping("/{id}")
	public String getUserByID(@PathVariable("id") Long id) {
		JSONObject user = this.users.getJSONObject(Long.toString(id));
		return user.toString();
	}

	@PutMapping("/{id}")
	public String putUser(@PathVariable("id") Long id, @RequestBody User user) {
		JSONObject user_json = new JSONObject();
		user_json.put("nome", user.getNome());
		user_json.put("login", user.getLogin());
		this.users.put(Long.toString(id), user_json);
		writeJson();
		return "Usuário atualizado com sucesso.";
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		this.users.remove(Long.toString(id));
		writeJson();
		return "Usuário removido com sucesso.";
	}
}
