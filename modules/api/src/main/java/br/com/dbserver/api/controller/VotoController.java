package br.com.dbserver.api.controller;

import br.com.dbserver.api.dto.VotoCreateDTO;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/votos")
@Tag(name = "Votos", description = "Operações para gerenciar votos.")
public class VotoController {
    
    private static final Logger log = LoggerFactory.getLogger(VotoController.class);
    
    private final VotoService votoService;
    
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    @Operation(
        summary = "Registrar voto.",
        description = "Registra um novo voto."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos."),
        @ApiResponse(responseCode = "409", description = "Membro já votou nesta assembleia.")
    })
    public ResponseEntity<VotoDTO> register(@Valid @RequestBody VotoCreateDTO dto) {
        
        log.info("Registrando voto para assembleia: {}, membro: {}", dto.getAssembleiaId(), dto.getMembroId());
        
        VotoDTO created = votoService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/pauta/{pautaId}")
    @Operation(
        summary = "Obter resultado da votação por pauta.",
        description = "Retorna o resultado consolidado da votação de uma pauta, podendo filtrar por assembleia e/ou membro."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado retornado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada.")
    })
    public ResponseEntity<Map<String, Object>> getResultsByPautaId(
            @Parameter(description = "Identificador da pauta.") @PathVariable UUID pautaId,
            @Parameter(description = "Identificador da assembleia.") @RequestParam(required = false) UUID assembleiaId,
            @Parameter(description = "Identificador do membro.") @RequestParam(required = false) UUID membroId) {
        
        log.debug("Obtendo resultados de votação para pauta: {}, assembleia: {}, membro: {}", pautaId, assembleiaId, membroId);
        
        Map<String, Object> results = votoService.getResultsByPautaId(pautaId, assembleiaId, membroId);
        return ResponseEntity.ok(results);
    }
}