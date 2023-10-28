package com.api.usuario.services;

import com.api.usuario.dtos.UsuarioDto;
import com.api.usuario.dtos.UsuarioRecuperaSenhaDto;
import com.api.usuario.models.SenhaToken;
import com.api.usuario.models.Usuario;
import com.api.usuario.repositories.SenhaTokenRepository;
import com.api.usuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SenhaTokenRepository senhaTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailRedefinirSenhaService emailRedefinirSenhaService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }

    @Transactional
    public ResponseEntity salvarUsuario(UsuarioDto usuarioDto) {

        var buscaUsuarioPeloEmail = usuarioRepository.findByEmailIgnoreCase(usuarioDto.email());

        if (!buscaUsuarioPeloEmail.isEmpty()) {
            return ResponseEntity.ok("E-mail já cadastrado");
        }

        Usuario usuario = new Usuario(usuarioDto);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setData_criacao(OffsetDateTime.now());
        var novoUsuario = usuarioRepository.save(usuario);

        return ResponseEntity.ok(novoUsuario);
    }

    @Transactional
    public ResponseEntity alterarUsuario(Long id, UsuarioDto usuarioDto) {

        var usuario = usuarioRepository.getReferenceById(id);

        if (usuarioDto.nome() != null) {
            usuario.setNome(usuarioDto.nome());
        }
        if (usuarioDto.email() != null) {
            var buscaUsuarioPeloEmail = usuarioRepository.findByEmailIgnoreCase(usuarioDto.email());

            if (buscaUsuarioPeloEmail.isPresent()) {
                return ResponseEntity.ok("Email já cadastrado");
            }
            usuario.setEmail(usuarioDto.email());
        }

        if (usuarioDto.senha() != null) {
            var novaSenha = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(novaSenha);
        }
        usuario.setData_atualizacao(OffsetDateTime.now());
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    @Transactional
    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public String gerarLinkParaAtualizarSenha(UsuarioRecuperaSenhaDto usuarioRecuperarSenhaDto) {

        var usuario = usuarioRepository.findByEmailIgnoreCase(usuarioRecuperarSenhaDto.email());

        return gerarToken(usuario.get());
    }

    public String gerarToken(Usuario usuario) {

        var token = new SenhaToken();

        token.setToken(UUID.randomUUID().toString());
        token.setCriadoEm(OffsetDateTime.now());
        token.setExpiraEm(OffsetDateTime.now().plusMinutes(30));
        token.setUsuario(usuario);

        senhaTokenRepository.save(token);

        return token.getToken();
    }

    public ResponseEntity<Object> atualizarSenha(String token, String senha) {

        Optional<SenhaToken> tokenOptional = senhaTokenRepository.findByToken(token);
        if(tokenOptional.isEmpty()) {
            return ResponseEntity.ok().body("Token inválido!");
        }

        if(tokenOptional.get().getConfirmadoEm() != null) {
            return ResponseEntity.ok().body("Token já foi utilizado, solicite novamente alteração de senha");
        }

        if(tokenOptional.get().getExpiraEm().isBefore(OffsetDateTime.now())) {
            return ResponseEntity.ok().body("Token expirado, solicite novamente alteração de senha");
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(tokenOptional.get().getUsuario().getId());
        usuarioOptional.get().setSenha(passwordEncoder.encode(senha));
        usuarioRepository.save(usuarioOptional.get());

        tokenOptional.get().setConfirmadoEm(OffsetDateTime.now());
        senhaTokenRepository.save(tokenOptional.get());

        return ResponseEntity.ok().body("Senha atualizada com sucesso!");
    }
}
