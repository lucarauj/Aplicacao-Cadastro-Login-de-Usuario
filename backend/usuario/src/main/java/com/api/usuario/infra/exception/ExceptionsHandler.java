package com.api.usuario.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> error404() {
        return new ResponseEntity<String>("Não encontrado", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosError::new).toList());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> error500(EmptyResultDataAccessException ex) {
        return new ResponseEntity<String>("Não encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private record DadosError(String campo, String mensagem) {
        public DadosError(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
