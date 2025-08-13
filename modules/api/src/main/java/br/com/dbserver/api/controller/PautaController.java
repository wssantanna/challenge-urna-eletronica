package br.com.dbserver.api.controller;

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
        @ApiResponse(responseCode = "200", description = "Lista de pautas retornada com sucesso.")
    })
    public Page<PautaDTO> findBy(
            @Parameter(description = "Texto para busca no título ou descrição.") 
            @RequestParam(required = false) String search,
            @Parameter(description = "Data/hora inicial do intervalo.") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @Parameter(description = "Data/hora final do intervalo.") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            Pageable pageable) {
        
        log.debug("Buscando pautas com filtros - search: '{}', dataInicio: {}, dataFim: {}", search, dataInicio, dataFim);
        
        return pautaService.findBy(search, dataInicio, dataFim, pageable);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter pauta pelo identificador.",
        description = "Retorna os detalhes de uma pauta pelo seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pauta encontrada."),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada.")
    })
    public ResponseEntity<PautaDTO> findById(
            @Parameter(description = "Identificador da pauta.") @PathVariable UUID id) {
        
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
        @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    public ResponseEntity<PautaDTO> create(@Valid @RequestBody PautaCreateDTO dto) {
        
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
        @ApiResponse(responseCode = "200", description = "Pauta atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada.")
    })
    public ResponseEntity<PautaDTO> updateById(
            @Parameter(description = "Identificador da pauta.") @PathVariable UUID id,
            @Valid @RequestBody PautaCreateDTO dto) {
        
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
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Identificador da pauta.") @PathVariable UUID id) {
        
        log.info("Excluindo pauta: {}", id);
        
        if (pautaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}