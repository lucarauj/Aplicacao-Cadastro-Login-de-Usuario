package com.api.usuario.models;

import com.api.usuario.dtos.UsuarioDto;
import com.api.usuario.enums.EnumPerfilUsuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    @Embedded
    private EnderecoUsuario endereco;
    private EnumPerfilUsuario perfil = EnumPerfilUsuario.ROLE_USER;
    private Boolean confirmado = false;
    private OffsetDateTime data_criacao;
    private OffsetDateTime data_atualizacao;

    public Usuario(UsuarioDto usuarioDto) {
        this.nome = usuarioDto.nome();
        this.email = usuarioDto.email();
        this.senha = usuarioDto.senha();
        this.endereco = new EnderecoUsuario(usuarioDto.endereco());
    }
}
