package com.api.usuario.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoUsuarioDto(
        @NotBlank
        String logradouro,
        @NotBlank
        String numero,
        @NotBlank
        String cidade,
        @NotBlank
        String bairro,
        @NotBlank
        String uf,
        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String cep,
        String complemento
) { }
