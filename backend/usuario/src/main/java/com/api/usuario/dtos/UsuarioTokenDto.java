package com.api.usuario.dtos;

import org.springframework.http.ResponseEntity;

public record UsuarioTokenDto(
        ResponseEntity usuario,
        String token
) { }
