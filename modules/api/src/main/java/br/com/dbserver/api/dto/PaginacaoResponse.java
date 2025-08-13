package br.com.dbserver.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO wrapper para respostas paginadas com propriedades em português.
 * 
 * Esta classe encapsula os dados de uma página Spring Data, traduzindo
 * as propriedades para facilitar o entendimento pelos usuários da API.
 */
@Schema(description = "Resposta paginada")
public class PaginacaoResponse<T> {
    
    @Schema(description = "Lista de itens da página atual")
    private List<T> conteudo;
    
    @Schema(description = "Número da página atual (inicia em 0)", example = "0")
    private int numeroPagina;
    
    @Schema(description = "Total de itens por página", example = "10")
    private int totalItensPorPagina;
    
    @Schema(description = "Total de itens encontrados", example = "150")
    private long totalItens;
    
    @Schema(description = "Total de páginas disponíveis", example = "8")
    private int totalPaginas;
    
    @Schema(description = "Informações de paginação")
    private PaginacaoInfo paginacao;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public PaginacaoResponse() {}

    /**
     * Construtor que recebe uma Page do Spring Data e converte para o formato traduzido.
     *
     * @param page página do Spring Data
     */
    public PaginacaoResponse(Page<T> page) {
        this.conteudo = page.getContent();
        this.numeroPagina = page.getNumber();
        this.totalItensPorPagina = page.getSize();
        this.totalItens = page.getTotalElements();
        this.totalPaginas = page.getTotalPages();
        this.paginacao = new PaginacaoInfo(page.getPageable());
    }

    /**
     * Método estático para criar uma instância a partir de uma Page do Spring Data.
     *
     * @param <T> tipo dos dados
     * @param page página do Spring Data
     * @return instância de PaginacaoResponse
     */
    public static <T> PaginacaoResponse<T> of(Page<T> page) {
        return new PaginacaoResponse<>(page);
    }

    public List<T> getConteudo() {
        return conteudo;
    }

    public void setConteudo(List<T> conteudo) {
        this.conteudo = conteudo;
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public int getTotalItensPorPagina() {
        return totalItensPorPagina;
    }

    public void setTotalItensPorPagina(int totalItensPorPagina) {
        this.totalItensPorPagina = totalItensPorPagina;
    }

    public long getTotalItens() {
        return totalItens;
    }

    public void setTotalItens(long totalItens) {
        this.totalItens = totalItens;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public PaginacaoInfo getPaginacao() {
        return paginacao;
    }

    public void setPaginacao(PaginacaoInfo paginacao) {
        this.paginacao = paginacao;
    }

    /**
     * Classe interna para informações detalhadas de paginação.
     */
    @Schema(description = "Informações detalhadas de paginação")
    public static class PaginacaoInfo {
        
        @Schema(description = "Número da página atual (inicia em 0)", example = "0")
        private int numeroPagina;
        
        @Schema(description = "Total de itens por página", example = "10")
        private int totalItensPorPagina;
        
        @Schema(description = "Deslocamento dos itens", example = "0")
        private long deslocamento;
        
        @Schema(description = "Informações de ordenação")
        private OrdenacaoInfo ordenacao;

        public PaginacaoInfo() {}

        public PaginacaoInfo(org.springframework.data.domain.Pageable pageable) {
            this.numeroPagina = pageable.getPageNumber();
            this.totalItensPorPagina = pageable.getPageSize();
            this.deslocamento = pageable.getOffset();
            this.ordenacao = new OrdenacaoInfo(pageable.getSort());
        }

        public int getNumeroPagina() {
            return numeroPagina;
        }

        public void setNumeroPagina(int numeroPagina) {
            this.numeroPagina = numeroPagina;
        }

        public int getTotalItensPorPagina() {
            return totalItensPorPagina;
        }

        public void setTotalItensPorPagina(int totalItensPorPagina) {
            this.totalItensPorPagina = totalItensPorPagina;
        }

        public long getDeslocamento() {
            return deslocamento;
        }

        public void setDeslocamento(long deslocamento) {
            this.deslocamento = deslocamento;
        }

        public OrdenacaoInfo getOrdenacao() {
            return ordenacao;
        }

        public void setOrdenacao(OrdenacaoInfo ordenacao) {
            this.ordenacao = ordenacao;
        }
    }

    /**
     * Classe interna para informações de ordenação.
     */
    @Schema(description = "Informações de ordenação")
    public static class OrdenacaoInfo {
        
        @Schema(description = "Tipo de ordenação: 'asc' (crescente), 'desc' (decrescente) ou 'sem ordenação'", example = "asc")
        private String ordenacao;

        public OrdenacaoInfo() {}

        public OrdenacaoInfo(org.springframework.data.domain.Sort sort) {
            if (sort.isUnsorted() || sort.isEmpty()) {
                this.ordenacao = "sem ordenação";
            } else {
                // Pega a primeira direção de ordenação
                org.springframework.data.domain.Sort.Direction direction = sort.iterator().next().getDirection();
                this.ordenacao = direction.isAscending() ? "asc" : "desc";
            }
        }

        public String getOrdenacao() {
            return ordenacao;
        }

        public void setOrdenacao(String ordenacao) {
            this.ordenacao = ordenacao;
        }
    }
}