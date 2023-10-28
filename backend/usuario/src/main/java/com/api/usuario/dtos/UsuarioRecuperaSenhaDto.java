package com.api.usuario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRecuperaSenhaDto(
        @NotBlank
        @Email
        String email
) { }
