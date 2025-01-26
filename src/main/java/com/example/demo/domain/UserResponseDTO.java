package com.example.demo.domain;

public record UserResponseDTO(String id, String login, String name){
    public UserResponseDTO (User user) {
        this(user.getId(), user.getLogin(), user.getName());
    }
}