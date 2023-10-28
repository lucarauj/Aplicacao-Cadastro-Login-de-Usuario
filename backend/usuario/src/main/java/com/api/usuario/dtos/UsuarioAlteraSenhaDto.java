package com.api.usuario.dtos;

import jakarta.validation.constraints.NotBlank;

public record UsuarioAlteraSenhaDto(
        @NotBlank
        String senha
) { }
