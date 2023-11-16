# Aplicação de Cadastro e Login de Usuário com Autenticação JWT e envio de E-mail

<hr>

# Backend

## Dependências

- Spring Web
- Spring Data JPA
- Validation
- PostgreSQL Driver
- Lombok
- Spring Security
- Java Mail Sender
- Java JWT

## Anotações

- @AllArgsConstructor
- @Bean
- @Component
- @Configuration
- @Data
- @DeleteMapping
- @Email
- @Embeddable
- @Embedded
- @EnableWebSecurity
- @Entity
- @EqualsAndHashCode(of = "id")
- @ExceptionHandler
- @GeneratedValue(strategy = GenerationType.IDENTITY)
- @GetMapping
- @Getter
- @Id
- @JoinColumn
- @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
- @ManyToOne
- @NotBlank
- @NoArgsConstructor
- @Pattern(regexp = "\d{8}")
- @PostMapping
- @PutMapping
- @RequestBody
- @Repository
- @RestController
- @ResponseBody
- @RequestMapping
- @RestControllerAdvice
- @Service
- @Setter
- @Table
- @Transactional
- @Valid
- @Value

<br>

## Populando o banco de dados com Admin


```
insert into usuarios (confirmado, data_atualizacao, data_criacao, email, nome, perfil, senha, logradouro, numero, cidade, bairro, uf, cep, complemento) 
values ( 
	false, 
	null, 
	'2023-10-28 15:31:36.653961', 
	'luis@gmail.com', 
	'Luis', 
	'0', 
	'$2a$10$Uo4JADUuF7rr3dKq6S35UOCa515wCFaG8AUeEVlRWouDkh/lxMW3m',
	'rua Getúlio',
    	'17',
    	'Campo Belo',
   	'centro',
    	'MG',
    	'34420090',
    	'N/a'
)
```