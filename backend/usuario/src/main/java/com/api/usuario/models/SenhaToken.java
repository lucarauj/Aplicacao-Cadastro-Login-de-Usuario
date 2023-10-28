package com.api.usuario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity (name = "SenhaToken")
@Table(name = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenhaToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private OffsetDateTime criadoEm;
    private OffsetDateTime expiraEm;
    private OffsetDateTime confirmadoEm;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
