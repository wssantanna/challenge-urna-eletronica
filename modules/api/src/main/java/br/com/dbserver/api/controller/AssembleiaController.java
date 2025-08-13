package br.com.dbserver.api.controller;

import br.com.dbserver.api.domain.entities.StatusAssembleia;
import br.com.dbserver.api.dto.AssembleiaCreateDTO;
import br.com.dbserver.api.dto.AssembleiaDTO;
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
        @ApiResponse(responseCode = "200", description = "Lista de assembleias retornada com sucesso.")
    })
    public Page<AssembleiaDTO> findBy(
            @Parameter(description = "Status da assembleia para filtro.") 
            @RequestParam(required = false) StatusAssembleia status,
            @Parameter(description = "Data/hora inicial do intervalo.") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @Parameter(description = "Data/hora final do intervalo.") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            Pageable pageable) {
        
        log.debug("Buscando assembleias com filtros - status: {}, dataInicio: {}, dataFim: {}", status, dataInicio, dataFim);
        
        return assembleiaService.findBy(status, dataInicio, dataFim, pageable);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter assembleia pelo identificador.",
        description = "Retorna os detalhes de uma assembleia pelo seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assembleia encontrada."),
        @ApiResponse(responseCode = "404", description = "Assembleia não encontrada.")
    })
    public ResponseEntity<AssembleiaDTO> findById(
            @Parameter(description = "Identificador da assembleia.") @PathVariable UUID id) {
        
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
        @ApiResponse(responseCode = "201", description = "Assembleia criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    public ResponseEntity<AssembleiaDTO> create(@Valid @RequestBody AssembleiaCreateDTO dto) {
        
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
        @ApiResponse(responseCode = "200", description = "Assembleia atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Assembleia não encontrada.")
    })
    public ResponseEntity<AssembleiaDTO> updateById(
            @Parameter(description = "Identificador da assembleia.") @PathVariable UUID id,
            @Valid @RequestBody AssembleiaCreateDTO dto) {
        
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
        @ApiResponse(responseCode = "404", description = "Assembleia não encontrada")
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Identificador da assembleia.") @PathVariable UUID id) {
        
        log.info("Excluindo assembleia: {}", id);
        
        if (assembleiaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}