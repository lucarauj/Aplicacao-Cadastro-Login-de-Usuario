package com.api.usuario.controllers;

import com.api.usuario.dtos.*;
import com.api.usuario.infra.security.SecurityToken;
import com.api.usuario.models.Usuario;
import com.api.usuario.repositories.SenhaTokenRepository;
import com.api.usuario.repositories.UsuarioRepository;
import com.api.usuario.services.EmailCadastroService;
import com.api.usuario.services.EmailRedefinirSenhaService;
import com.api.usuario.services.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private SecurityToken securityToken;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SenhaTokenRepository senhaTokenRepository;

    @Autowired
    private EmailCadastroService emailCadastroService;

    @Autowired
    private EmailRedefinirSenhaService emailRedefinirSenhaService;

    @PostMapping("/cadastrar-usuario")
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {

        var buscaUsuarioPeloEmail = usuarioRepository.findByEmailIgnoreCase(usuarioDto.email());

        if (!buscaUsuarioPeloEmail.isEmpty()) {
            return ResponseEntity.ok("E-mail já cadastrado");
        }

        var novoUsuario = usuarioService.salvarUsuario(usuarioDto);

        var authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDto.email(), usuarioDto.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = securityToken.gerarToken((Usuario)authentication.getPrincipal());

        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto(novoUsuario, tokenJWT);

        String destinatario = usuarioDto.email();
        String assunto = "Bem-vindo ao 'Meu site'";
        String nome = usuarioDto.nome();
        String token = tokenJWT;

        try {
            emailCadastroService.enviarEmail(destinatario, assunto, nome, token);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(usuarioTokenDto);
    }

    @PutMapping("/confirmar-usuario/{token}/{id}")
    public ResponseEntity confirmarUsuario(@PathVariable("token") String token, @PathVariable("id") Long id) {

        var usuario = senhaTokenRepository.findByToken(token);

        if(usuario.isEmpty()) {
            return ResponseEntity.ok().body("Usuário não existe para o token informado!");
        }

        var confirmaUsuario = usuarioService.confirmarUsuario(token, id);

        return ResponseEntity.ok().body(confirmaUsuario);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity recuperarSenha(@RequestBody @Valid UsuarioRecuperaSenhaDto usuarioRecuperarSenhaDto) {

        var usuario = usuarioRepository.findByEmailIgnoreCase(usuarioRecuperarSenhaDto.email());

        if(usuario.isEmpty()) {
            return ResponseEntity.ok().body("Usuário não existe para o e-mail informado!");
        }

        var tokenUsuario = usuarioService.gerarLinkParaAtualizarSenha(usuarioRecuperarSenhaDto);

        String link = "recuperar-senha/" + tokenUsuario;
        String destinatario = usuarioRecuperarSenhaDto.email();
        String assunto = "Solicitação de Alteração de Senha";
        String nome = usuario.get().getNome();

        try {
            emailRedefinirSenhaService.enviarEmailRedefinirSenha(destinatario, assunto, nome, link);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body("E-mail enviado com sucesso!");
    }

    @PutMapping("/alterar-senha/{token}")
    public ResponseEntity alterarSenha(@PathVariable String token, @RequestBody @Valid UsuarioAlteraSenhaDto usuarioAlterarSenhaDto) {

        var alteraSenha = usuarioService.atualizarSenha(token, usuarioAlterarSenhaDto.senha());

        return ResponseEntity.ok().body(alteraSenha);
    }

//    @GetMapping("/listar-usuarios")
//    @ResponseBody
//    public ResponseEntity listarUsuarios() {
//
//        var usuarios = usuarioRepository.findAll();
//        if(!usuarios.isEmpty()) {
//            return ResponseEntity.ok(usuarios);
//        }
//        return ResponseEntity.ok("Não há usuários cadastrados");
//    }
//
//    @GetMapping("/listar-usuario/{id}")
//    @ResponseBody
//    public ResponseEntity pesquisarProdutosPorId(@PathVariable Long id) {
//
//        var usuario = usuarioRepository.getReferenceById(id);
//        return ResponseEntity.ok(usuario);
//    }
//
//    @PutMapping("/atualizar-usuario/{id}")
//    public ResponseEntity atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
//
//        var usuario = usuarioService.alterarUsuario(id, usuarioDto);
//        return ResponseEntity.ok(usuario);
//    }
//
    @DeleteMapping("/deletar-usuario/{id}")
    public ResponseEntity deletarUsuario(@PathVariable Long id) {

        var usuario = usuarioRepository.findById(id);
        if(usuario.isEmpty()) {
            return ResponseEntity.ok().body("Não existe usuário para o ID informado!");
        }

        usuarioService.excluirUsuario(id);
        return ResponseEntity.ok("Usuário excluído com sucesso!");
    }
}
