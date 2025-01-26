package com.example.demo.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank
        String login,

        @NotBlank
        String name
) {
}
