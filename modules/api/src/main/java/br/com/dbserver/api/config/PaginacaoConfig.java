package br.com.dbserver.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuração para paginação padrão da aplicação.
 * 
 * Define parâmetros traduzidos para português e tamanho padrão de 10 itens por página.
 */
@Configuration
public class PaginacaoConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(@org.springframework.lang.NonNull List<HandlerMethodArgumentResolver> resolvers) {
        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
        sortResolver.setSortParameter("ordenarPor");
        
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver(sortResolver);
        pageableResolver.setPageParameterName("paginaAtual");
        pageableResolver.setSizeParameterName("itensPorPagina");
        pageableResolver.setOneIndexedParameters(false);
        pageableResolver.setMaxPageSize(100);
        pageableResolver.setFallbackPageable(
            org.springframework.data.domain.PageRequest.of(0, 10)
        );
        
        resolvers.add(pageableResolver);
    }
}