package com.example.demo.domain;

public record RegisterDTO(String login, String name, String password, UserRole role) {
}
