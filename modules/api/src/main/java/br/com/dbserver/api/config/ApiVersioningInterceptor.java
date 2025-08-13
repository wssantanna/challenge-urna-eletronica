package br.com.dbserver.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interceptor responsável por adicionar headers de versionamento automaticamente.
 * 
 * Este interceptor:
 * - Detecta a versão da API sendo chamada via URL
 * - Adiciona headers informativos sobre versionamento
 * - Aplica headers de deprecação quando necessário
 * - Registra logs para monitoramento de uso por versão
 */
public class ApiVersioningInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(ApiVersioningInterceptor.class);
    
    private final ApiVersioningConfig config;
    private final Pattern versionPattern = Pattern.compile("/api/(v\\d+)/");
    
    public ApiVersioningInterceptor(ApiVersioningConfig config) {
        this.config = config;
    }
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, 
                           @NonNull HttpServletResponse response, 
                           @NonNull Object handler) {
        
        String requestURI = request.getRequestURI();
        String version = extractVersionFromUrl(requestURI);
        
        if (version != null) {
            addVersionHeaders(response, version, requestURI);
            logApiUsage(request, version);
        }
        
        return true;
    }
    
    /**
     * Extrai a versão da API a partir da URL.
     */
    private String extractVersionFromUrl(String url) {
        Matcher matcher = versionPattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    /**
     * Adiciona headers de versionamento na resposta.
     */
    private void addVersionHeaders(HttpServletResponse response, String version, String requestURI) {
        // Header básico com a versão
        response.addHeader("X-API-Version", version);
        
        // Headers para versão atual
        if (config.getCurrentVersion().equals(version)) {
            response.addHeader("X-API-Status", "current");
        }
        
        // Headers para versões deprecadas
        if (config.isVersionDeprecated(version)) {
            ApiVersioningConfig.DeprecationInfo deprecationInfo = config.getDeprecationInfo(version);
            
            response.addHeader("X-API-Status", "deprecated");
            response.addHeader("X-API-Deprecated", deprecationInfo.getMessage());
            
            // Formato RFC 7234 para Sunset header
            String sunsetDate = deprecationInfo.getSunsetDate()
                    .format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'"));
            response.addHeader("Sunset", sunsetDate);
            
            // Link para versão sucessora
            if (deprecationInfo.getSuccessorVersion() != null) {
                response.addHeader("Link", String.format("<%s>; rel=\"successor-version\"", 
                        deprecationInfo.getSuccessorVersion()));
            }
        }
        
        // Header com versões suportadas
        response.addHeader("X-API-Supported-Versions", String.join(", ", config.getSupportedVersions()));
    }
    
    /**
     * Registra logs estruturados para monitoramento de uso por versão.
     */
    private void logApiUsage(HttpServletRequest request, String version) {
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String clientIp = getClientIpAddress(request);
        
        log.info("API_USAGE version={} method={} endpoint={} client_ip={} user_agent={}", 
                version, method, endpoint, clientIp, userAgent);
        
        // Log específico para versões deprecadas
        if (config.isVersionDeprecated(version)) {
            log.warn("DEPRECATED_API_USAGE version={} endpoint={} client_ip={} - Client using deprecated version", 
                    version, endpoint, clientIp);
        }
    }
    
    /**
     * Obtém o endereço IP real do cliente considerando proxies e load balancers.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}