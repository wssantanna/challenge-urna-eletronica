# &lt;db&gt; Challenge Urna Eletrônica

Desafio proposto pela empresa DB Server para o desenvolvimento de uma aplicação de Urna Eletrônica.

## Tecnologias

- Java 17
- Spring Boot 3.5.4
- Maven 3
- Docker

## Diagrama de entidades

![Diagrama de entidades do projeto Urna Eletrônica](https://iili.io/FZd4jb2.webp)

<br>

[Visualização pelo Mermaid Chart](https://www.mermaidchart.com/app/projects/0439d6f5-5a41-48d7-9b18-fec00a458679/diagrams/1fb9e0a7-fd8d-4f39-a5d9-30a7162cb913/version/v0.1/edit)

<br>

## Execução

### Docker (Recomendado)

```bash
docker-compose up --build
```

### Maven

```bash
mvn spring-boot:run
```

### Testes

```bash
mvn test
```

## Acesso

### Local

Após a inicialização do projeto:

| Descrição | URL |
|-- | -- | 
| Documentação | `http://localhost:8080/swagger-ui/index.html` |
