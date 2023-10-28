package com.api.usuario.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()

                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/autenticacao").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/cadastrar-usuario").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/listar-usuario/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/atualizar-usuario/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/recuperar-senha").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/alterar-senha/{token}").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/listar-usuarios").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/deletar-usuario/{id}").hasAuthority("ROLE_ADMIN")

                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                .anyRequest().authenticated()
                .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

