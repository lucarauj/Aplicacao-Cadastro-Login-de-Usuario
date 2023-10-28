package com.api.usuario.controllers;

import com.api.usuario.dtos.TokenDto;
import com.api.usuario.dtos.UsuarioAutenticaDto;
import com.api.usuario.infra.security.SecurityToken;
import com.api.usuario.models.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private SecurityToken securityToken;

    @PostMapping("/autenticacao")
    public ResponseEntity autenticarUsuario(@RequestBody @Valid UsuarioAutenticaDto usuarioAutenticacaoDto) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(usuarioAutenticacaoDto.email(), usuarioAutenticacaoDto.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = securityToken.gerarToken((Usuario)authentication.getPrincipal());
        return ResponseEntity.ok(new TokenDto(tokenJWT));
    }
}
