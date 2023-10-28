package com.api.usuario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioAutenticaDto (
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha
) { }
