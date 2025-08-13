package br.com.dbserver.api.controller;

import br.com.dbserver.api.dto.VotoCreateDTO;
import br.com.dbserver.api.dto.VotoCreateV2DTO;
import br.com.dbserver.api.dto.VotoDTO;
import br.com.dbserver.api.service.VotoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Votos", description = "Operações para gerenciar votos (V1 e V2).")
public class VotoController {
    
    private static final Logger log = LoggerFactory.getLogger(VotoController.class);
    
    private final VotoService votoService;
    
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping("/v1/votos")
    @Deprecated(since = "2.0", forRemoval = true)
    @Operation(
        summary = "Registrar voto (DEPRECATED - Use a versão 2).",
        description = "Registra um novo voto. ATENÇÃO: Esta versão está obsoleta. Use /api/v2/votos para novos desenvolvimentos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Voto registrado com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Voto registrado",
                    value = """
                    {
                        "idVoto": "550e8400-e29b-41d4-a716-446655440003",
                        "assembleia": {
                            "idAssembleia": "550e8400-e29b-41d4-a716-446655440002",
                            "status": "ATIVA"
                        },
                        "membroId": "550e8400-e29b-41d4-a716-446655440004",
                        "decisao": "Concordo",
                        "registradoEm": "2024-01-15T14:30:00Z"
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
                        "caminho": "/api/v1/votos",
                        "erros": [
                            {
                                "campo": "assembleiaId",
                                "mensagem": "O ID da assembleia é obrigatório"
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Membro já votou nesta assembleia.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Voto duplicado",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "VOTO_JA_REGISTRADO",
                        "mensagem": "Este membro já registrou seu voto nesta assembleia",
                        "caminho": "/api/v1/votos"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<VotoDTO> register(
            @RequestBody(
                description = "Dados para registro do voto",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Exemplo de registro de voto",
                        value = """
                        {
                            "assembleiaId": "550e8400-e29b-41d4-a716-446655440002",
                            "membroId": "550e8400-e29b-41d4-a716-446655440004",
                            "decisao": "Concordo"
                        }
                        """
                    )
                )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody VotoCreateDTO dto) {
        
        log.info("Registrando voto V1 (DEPRECATED) para assembleia: {}, membro: {}", dto.getAssembleiaId(), dto.getMembroId());
        
        VotoDTO created = votoService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/v2/votos")
    @Operation(
        summary = "Registrar voto.",
        description = "Registra um novo voto usando nome e o número de CPF do membro."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Voto registrado com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Voto registrado",
                    value = """
                    {
                        "idVoto": "550e8400-e29b-41d4-a716-446655440003",
                        "assembleia": {
                            "idAssembleia": "550e8400-e29b-41d4-a716-446655440002",
                            "status": "ATIVA"
                        },
                        "membroId": "550e8400-e29b-41d4-a716-446655440004",
                        "decisao": "Concordo",
                        "registradoEm": "2024-01-15T14:30:00Z"
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
                        "caminho": "/api/v2/votos",
                        "erros": [
                            {
                                "campo": "nome",
                                "mensagem": "O nome do membro é obrigatório"
                            },
                            {
                                "campo": "cpf",
                                "mensagem": "CPF deve conter exatamente 11 dígitos"
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Membro não encontrado ou nome não confere.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Membro não encontrado",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "MEMBRO_NAO_ENCONTRADO",
                        "mensagem": "Membro não encontrado com CPF: 12345678901",
                        "caminho": "/api/v2/votos"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Membro já votou nesta assembleia.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Erro - Voto duplicado",
                    value = """
                    {
                        "dataHora": "2024-01-15T10:30:00Z",
                        "codigoErro": "VOTO_JA_REGISTRADO",
                        "mensagem": "Este membro já registrou seu voto nesta assembleia",
                        "caminho": "/api/v2/votos"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<VotoDTO> registerV2(
            @RequestBody(
                description = "Dados para registro do voto (V2)",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Exemplo de registro de voto V2",
                        value = """
                        {
                            "assembleiaId": "550e8400-e29b-41d4-a716-446655440002",
                            "nome": "João da Silva",
                            "cpf": "12345678901",
                            "decisao": "Concordo"
                        }
                        """
                    )
                )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody VotoCreateV2DTO dto) {
        
        log.info("Registrando voto V2 para assembleia: {}, membro: {} - {}", 
                dto.getAssembleiaId(), dto.getNome(), dto.getCpf());
        
        VotoDTO created = votoService.registerV2(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/v1/votos/pauta/{pautaId}")
    @Operation(
        summary = "Obter resultado da votação por pauta.",
        description = "Retorna o resultado consolidado da votação de uma pauta, podendo filtrar por assembleia e/ou membro."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Resultado retornado com sucesso.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Resultado da votação",
                    value = """
                    {
                        "pautaId": "550e8400-e29b-41d4-a716-446655440001",
                        "titulo": "Aprovação do orçamento anual",
                        "totalVotos": 150,
                        "votosSim": 85,
                        "votosNao": 65,
                        "percentualSim": 56.67,
                        "percentualNao": 43.33,
                        "resultado": "APROVADO",
                        "detalhes": [
                            {
                                "assembleiaId": "550e8400-e29b-41d4-a716-446655440002",
                                "statusAssembleia": "FINALIZADA",
                                "totalVotosAssembleia": 150
                            }
                        ]
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
                        "caminho": "/api/v1/votos/pauta/550e8400-e29b-41d4-a716-446655440001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getResultsByPautaId(
            @Parameter(description = "Identificador da pauta.", example = "550e8400-e29b-41d4-a716-446655440001") @PathVariable UUID pautaId,
            @Parameter(description = "Identificador da assembleia.", example = "550e8400-e29b-41d4-a716-446655440002") @RequestParam(required = false) UUID assembleiaId,
            @Parameter(description = "Identificador do membro.", example = "550e8400-e29b-41d4-a716-446655440005") @RequestParam(required = false) UUID membroId) {
        
        log.debug("Obtendo resultados de votação para pauta: {}, assembleia: {}, membro: {}", pautaId, assembleiaId, membroId);
        
        Map<String, Object> results = votoService.getResultsByPautaId(pautaId, assembleiaId, membroId);
        return ResponseEntity.ok(results);
    }
}