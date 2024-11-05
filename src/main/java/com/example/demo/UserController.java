package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class UserController {

	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return "Hello World";
	}

	@PostMapping("/UserManagement/rest/UserService/users")
	public String post(@RequestBody User user){
		return "Post: " + user.getId();
	}

	@GetMapping("/UserManagement/rest/UserService/users")
	public String Teste() {
		return "Teste";
	}

	@GetMapping("/UserManagement/rest/UserService/users/{id}")
	public String getUserByID(@PathVariable("id") Long id) {
		return "Teste";
	}
}
