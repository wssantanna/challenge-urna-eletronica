package br.com.dbserver.api.controller;

import br.com.dbserver.api.domain.entities.StatusAssembleia;
import br.com.dbserver.api.dto.AssembleiaCreateDTO;
import br.com.dbserver.api.dto.AssembleiaDTO;
import br.com.dbserver.api.dto.PaginacaoResponse;
import br.com.dbserver.api.service.AssembleiaService;
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
@RequestMapping("/api/v1/assembleias")
@Tag(name = "Assembleias", description = "Operações para gerenciar assembleias.")
public class AssembleiaController {
    
    private static final Logger log = LoggerFactory.getLogger(AssembleiaController.class);
    
    private final AssembleiaService assembleiaService;
    
    public AssembleiaController(AssembleiaService assembleiaService) {
        this.assembleiaService = assembleiaService;
    }

    @GetMapping
    @Operation(
        summary = "Listar assembleias.",
        description = "Retorna a lista paginada de assembleias, podendo filtrar por status e/ou intervalo de datas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de assembleias retornada com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Lista paginada de assembleias",
                    value = """
                    {
                        "conteudo": [
                            {
                                "idAssembleia": "550e8400-e29b-41d4-a716-446655440002",
                                "pauta": {
                                    "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                                    "titulo": "Aprovação do orçamento anual",
                                    "descricao": "Discussão e votação sobre o orçamento da empresa",
                                    "criadaEm": "2024-01-15T10:30:00Z"
                                },
                                "status": "Aberta",
                                "iniciadaEm": "2024-01-15T14:00:00Z",
                                "finalizadaEm": null
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
                                "ordenacao": "desc"
                            }
                        }
                    }
                    """
                )
            )
        )
    })
    public PaginacaoResponse<AssembleiaDTO> findBy(
            @Parameter(description = "Status da assembleia para filtro.", example = "Aberta") 
            @RequestParam(required = false) StatusAssembleia status,
            @Parameter(description = "Data/hora inicial do intervalo.", example = "2024-01-01T00:00:00Z") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @Parameter(description = "Data/hora final do intervalo.", example = "2024-12-31T23:59:59Z") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            @Parameter(description = "Parâmetros de paginação", example = "{\n  \"paginaAtual\": 0,\n  \"itensPorPagina\": 10,\n  \"ordenarPor\": [\"iniciadaEm,desc\"]\n}")
            Pageable pageable) {
        
        log.debug("Buscando assembleias com filtros - status: {}, dataInicio: {}, dataFim: {}", status, dataInicio, dataFim);
        
        Page<AssembleiaDTO> page = assembleiaService.findBy(status, dataInicio, dataFim, pageable);
        return PaginacaoResponse.of(page);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter assembleia pelo identificador.",
        description = "Retorna os detalhes de uma assembleia pelo seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Assembleia encontrada.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Assembleia encontrada",
                    value = """
                    {
                        "idAssembleia": "550e8400-e29b-41d4-a716-446655440002",
                        "pauta": {
                            "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                            "titulo": "Aprovação do orçamento anual",
                            "descricao": "Discussão e votação sobre o orçamento da empresa",
                            "criadaEm": "2024-01-15T10:30:00Z"
                        },
                        "status": "ATIVA",
                        "iniciadaEm": "2024-01-15T14:00:00Z",
                        "finalizadaEm": null
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Assembleia não encontrada.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Assembleia não encontrada",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "ASSEMBLEIA_NAO_ENCONTRADA",
                        "mensagem": "Assembleia não encontrada",
                        "caminho": "/api/v1/assembleias/550e8400-e29b-41d4-a716-446655440002"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<AssembleiaDTO> findById(
            @Parameter(description = "Identificador da assembleia.", example = "550e8400-e29b-41d4-a716-446655440002") @PathVariable UUID id) {
        
        log.debug("Buscando assembleia por id: {}", id);
        
        return assembleiaService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar assembleia.",
        description = "Cadastra uma nova assembleia."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Assembleia criada com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Assembleia criada",
                    value = """
                    {
                        "idAssembleia": "550e8400-e29b-41d4-a716-446655440002",
                        "pauta": {
                            "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                            "titulo": "Aprovação do orçamento anual",
                            "descricao": "Discussão e votação sobre o orçamento da empresa",
                            "criadaEm": "2024-01-15T10:30:00Z"
                        },
                        "status": "ATIVA",
                        "iniciadaEm": "2024-01-15T14:00:00Z",
                        "finalizadaEm": null
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
                        "caminho": "/api/v1/assembleias",
                        "erros": [
                            {
                                "campo": "pautaId",
                                "mensagem": "O ID da pauta é obrigatório"
                            }
                        ]
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<AssembleiaDTO> create(
            @RequestBody(
                description = "Dados para criação da assembleia",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Exemplo de criação de assembleia",
                        value = """
                        {
                            "pautaId": "550e8400-e29b-41d4-a716-446655440001"
                        }
                        """
                    )
                )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody AssembleiaCreateDTO dto) {
        
        log.info("Criando assembleia para pauta: {}", dto.getPautaId());
        
        AssembleiaDTO created = assembleiaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar assembleia.",
        description = "Atualiza os dados de uma assembleia existente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Assembleia atualizada com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Assembleia atualizada",
                    value = """
                    {
                        "idAssembleia": "550e8400-e29b-41d4-a716-446655440002",
                        "pauta": {
                            "idPauta": "550e8400-e29b-41d4-a716-446655440001",
                            "titulo": "Aprovação do orçamento anual",
                            "descricao": "Discussão e votação sobre o orçamento da empresa",
                            "criadaEm": "2024-01-15T10:30:00Z"
                        },
                        "status": "Encerrada",
                        "iniciadaEm": "2024-01-15T14:00:00Z",
                        "finalizadaEm": "2024-01-15T16:00:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Assembleia não encontrada.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Assembleia não encontrada",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "ASSEMBLEIA_NAO_ENCONTRADA",
                        "mensagem": "Assembleia não encontrada",
                        "caminho": "/api/v1/assembleias/550e8400-e29b-41d4-a716-446655440002"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<AssembleiaDTO> updateById(
            @Parameter(description = "Identificador da assembleia.", example = "550e8400-e29b-41d4-a716-446655440002") @PathVariable UUID id,
            @RequestBody(
                description = "Dados para atualização da assembleia",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Exemplo de atualização de assembleia",
                        value = """
                        {
                            "pautaId": "550e8400-e29b-41d4-a716-446655440001"
                        }
                        """
                    )
                )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody AssembleiaCreateDTO dto) {
        
        log.info("Atualizando assembleia: {}", id);
        
        return assembleiaService.updateById(id, dto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir assembleia.",
        description = "Remove uma assembleia existente pelo seu identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Assembleia excluída com sucesso"),
        @ApiResponse(
            responseCode = "404", 
            description = "Assembleia não encontrada",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Assembleia não encontrada",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "ASSEMBLEIA_NAO_ENCONTRADA",
                        "mensagem": "Assembleia não encontrada",
                        "caminho": "/api/v1/assembleias/550e8400-e29b-41d4-a716-446655440002"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Identificador da assembleia.", example = "550e8400-e29b-41d4-a716-446655440002") @PathVariable UUID id) {
        
        log.info("Excluindo assembleia: {}", id);
        
        if (assembleiaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}