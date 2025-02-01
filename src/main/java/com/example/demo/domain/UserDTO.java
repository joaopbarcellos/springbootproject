package com.example.demo.domain;

public record UserDTO(String id, String login, String name, String password, UserRole role) {
}
