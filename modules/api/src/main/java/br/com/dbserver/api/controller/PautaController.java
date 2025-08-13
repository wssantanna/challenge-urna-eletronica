package br.com.dbserver.api.controller;

import br.com.dbserver.api.dto.PaginacaoResponse;
import br.com.dbserver.api.dto.PautaCreateDTO;
import br.com.dbserver.api.dto.PautaDTO;
import br.com.dbserver.api.service.PautaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pautas")
@Tag(name = "Pautas", description = "Operações para gerenciar pautas.")
public class PautaController {
    
    private static final Logger log = LoggerFactory.getLogger(PautaController.class);
    
    private final PautaService pautaService;
    
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @GetMapping
    @Operation(
        summary = "Listar pautas.",
        description = "Retorna a lista paginada de pautas, podendo filtrar por busca de texto e/ou intervalo de datas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de pautas retornada com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Lista paginada de pautas",
                    value = """
                    {
                        "conteudo": [
                            {
                                "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                                "titulo": "Aprovação do orçamento anual",
                                "descricao": "Discussão e votação sobre o orçamento da empresa para o próximo ano",
                                "criadaEm": "2024-01-15T10:30:00Z"
                            }
                        ],
                        "numeroPagina": 0,
                        "totalItensPorPagina": 10,
                        "totalItens": 1,
                        "totalPaginas": 1,
                        "paginacao": {
                            "numeroPagina": 0,
                            "totalItensPorPagina": 10,
                            "deslocamento": 0,
                            "ordenacao": {
                                "ordenacao": "asc"
                            }
                        }
                    }
                    """
                )
            )
        )
    })
    public PaginacaoResponse<PautaDTO> findBy(
            @Parameter(description = "Texto para busca no título ou descrição.", example = "orçamento") 
            @RequestParam(required = false) String search,
            @Parameter(description = "Data/hora inicial do intervalo.", example = "2024-01-01T00:00:00Z") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @Parameter(description = "Data/hora final do intervalo.", example = "2024-12-31T23:59:59Z") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            @Parameter(description = "Parâmetros de paginação", example = "{\n  \"paginaAtual\": 0,\n  \"itensPorPagina\": 10,\n  \"ordenarPor\": [\"criadaEm,desc\"]\n}")
            Pageable pageable) {
        
        log.debug("Buscando pautas com filtros - search: '{}', dataInicio: {}, dataFim: {}", search, dataInicio, dataFim);
        
        Page<PautaDTO> page = pautaService.findBy(search, dataInicio, dataFim, pageable);
        return PaginacaoResponse.of(page);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter pauta pelo identificador.",
        description = "Retorna os detalhes de uma pauta pelo seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pauta encontrada.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Pauta encontrada",
                    value = """
                    {
                        "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                        "titulo": "Aprovação do orçamento anual",
                        "descricao": "Discussão e votação sobre o orçamento da empresa para o próximo ano",
                        "criadaEm": "2024-01-15T10:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Pauta não encontrada.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Pauta não encontrada",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "PAUTA_NAO_ENCONTRADA",
                        "mensagem": "Pauta não encontrada",
                        "caminho": "/api/v1/pautas/550e8400-e29b-41d4-a716-446655440001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<PautaDTO> findById(
            @Parameter(description = "Identificador da pauta.", example = "550e8400-e29b-41d4-a716-446655440001") @PathVariable UUID id) {
        
        log.debug("Buscando pauta por id: {}", id);
        
        return pautaService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar pauta.",
        description = "Cadastra uma nova pauta."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Pauta criada com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Pauta criada",
                    value = """
                    {
                        "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                        "titulo": "Aprovação do orçamento anual",
                        "descricao": "Discussão e votação sobre o orçamento da empresa para o próximo ano",
                        "criadaEm": "2024-01-15T10:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados inválidos.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro de validação",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "DADOS_INVALIDOS",
                        "mensagem": "Dados inválidos fornecidos",
                        "caminho": "/api/v1/pautas",
                        "erros": [
                            {
                                "campo": "titulo",
                                "mensagem": "O título da pauta é obrigatório"
                            }
                        ]
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<PautaDTO> create(
            @RequestBody(
                description = "Dados para criação da pauta",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Exemplo de criação de pauta",
                        value = """
                        {
                            "titulo": "Aprovação do orçamento anual",
                            "descricao": "Discussão e votação sobre o orçamento da empresa para o próximo ano"
                        }
                        """
                    )
                )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody PautaCreateDTO dto) {
        
        log.info("Criando pauta com título: '{}'", dto.getTitulo());
        
        PautaDTO created = pautaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar pauta.",
        description = "Atualiza os dados de uma pauta existente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pauta atualizada com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Pauta atualizada",
                    value = """
                    {
                        "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                        "titulo": "Aprovação do orçamento anual atualizado",
                        "descricao": "Discussão e votação sobre o orçamento da empresa para o próximo ano com ajustes",
                        "criadaEm": "2024-01-15T10:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Pauta não encontrada.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Pauta não encontrada",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "PAUTA_NAO_ENCONTRADA",
                        "mensagem": "Pauta não encontrada",
                        "caminho": "/api/v1/pautas/550e8400-e29b-41d4-a716-446655440001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<PautaDTO> updateById(
            @Parameter(description = "Identificador da pauta.", example = "550e8400-e29b-41d4-a716-446655440001") @PathVariable UUID id,
            @RequestBody(
                description = "Dados para atualização da pauta",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Exemplo de atualização de pauta",
                        value = """
                        {
                            "titulo": "Aprovação do orçamento anual atualizado",
                            "descricao": "Discussão e votação sobre o orçamento da empresa para o próximo ano com ajustes"
                        }
                        """
                    )
                )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody PautaCreateDTO dto) {
        
        log.info("Atualizando pauta: {}", id);
        
        return pautaService.updateById(id, dto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir pauta.",
        description = "Remove uma pauta existente pelo seu identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pauta excluída com sucesso"),
        @ApiResponse(
            responseCode = "404", 
            description = "Pauta não encontrada",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Pauta não encontrada",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "PAUTA_NAO_ENCONTRADA",
                        "mensagem": "Pauta não encontrada",
                        "caminho": "/api/v1/pautas/550e8400-e29b-41d4-a716-446655440001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Identificador da pauta.", example = "550e8400-e29b-41d4-a716-446655440001") @PathVariable UUID id) {
        
        log.info("Excluindo pauta: {}", id);
        
        if (pautaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}