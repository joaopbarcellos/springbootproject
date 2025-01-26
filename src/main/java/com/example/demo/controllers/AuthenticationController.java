package com.example.demo.controllers;

import com.example.demo.domain.AuthenticationDTO;
import com.example.demo.domain.LoginResponseDTO;
import com.example.demo.domain.RegisterDTO;
import com.example.demo.domain.User;
import com.example.demo.infra.security.TokenService;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.services.EmailValidator.isValidEmail;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data){
        if (data.login() == null) {
            return ResponseEntity.badRequest().body("Erro: Login está vazio.");
        }

        if (!isValidEmail(data.login())) {
            return ResponseEntity.badRequest().body("Erro: Email invalido.");
        }

        if (data.password() == null ) {
            return ResponseEntity.badRequest().body("Erro: A senha deve ser preenchida.");
        }

        if (data.password().length() < 6 || data.password().length() > 30) {
            return ResponseEntity.badRequest().body("Erro: A senha deve ter entre 6 a 30 caracteres");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        if (data.login() == null) {
            return ResponseEntity.badRequest().body("Erro: Login está vazio.");
        }

        if (!isValidEmail(data.login())) {
            return ResponseEntity.badRequest().body("Erro: Email invalido.");
        }

        if (data.name() == null) {
            return ResponseEntity.badRequest().body("Erro: O nome deve ser preenchido.");
        }

        if (data.password() == null ) {
            return ResponseEntity.badRequest().body("Erro: A senha deve ser preenchida.");
        }

        if (data.password().length() < 6 || data.password().length() > 30) {
            return ResponseEntity.badRequest().body("Erro: A senha deve ter entre 6 a 30 caracteres");
        }


        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), data.name(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}
