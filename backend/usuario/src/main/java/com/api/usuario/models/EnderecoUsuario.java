package com.api.usuario.models;

import com.api.usuario.dtos.EnderecoUsuarioDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EnderecoUsuario {

    private String logradouro;
    private String numero;
    private String cidade;
    private String bairro;
    private String uf;
    private String cep;
    private String complemento;

    public EnderecoUsuario(EnderecoUsuarioDto enderecoDto) {
        this.logradouro = enderecoDto.logradouro();
        this.numero = enderecoDto.numero();
        this.cidade = enderecoDto.cidade();
        this.bairro = enderecoDto.bairro();
        this.uf = enderecoDto.uf();
        this.cep = enderecoDto.cep();
        this.complemento = enderecoDto.complemento();
    }
}
