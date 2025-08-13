package br.com.dbserver.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/hello")
@Tag(name = "Hello", description = "Um breve resumo sobre o autor do código-fonte.")
public class HelloController {

    @GetMapping
    @Operation(
        summary = "Minha saudação.",
        description = "Apresenta um breve resumo das minhas informações pessoais e da minha trajetória profissional."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Dados retornados com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Mensagem de saudação",
                    value = "Hello World"
                )
            )
        )
    })
    public String hello() {
        return "Hello World";
    }
}