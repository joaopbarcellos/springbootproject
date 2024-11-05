package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class UserController {

	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}

	@GetMapping(value = "/")
	public String hello() {
		return "Hello World";
	}

	@PostMapping(value = "/post")
	public String post(){
		return "Post";
	}

	@GetMapping(value = "/teste")
	public String Teste() {
		return "Teste";
	}
}
