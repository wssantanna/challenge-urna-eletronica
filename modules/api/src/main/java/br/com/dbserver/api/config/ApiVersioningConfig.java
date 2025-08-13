package br.com.dbserver.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Configuração global de versionamento da API.
 * 
 * Esta classe centraliza as configurações de versões suportadas,
 * deprecadas e suas datas de descontinuação.
 */
@Configuration
@ConfigurationProperties(prefix = "api.versioning")
public class ApiVersioningConfig implements WebMvcConfigurer {
    
    /**
     * Versão atual da API (mais recente).
     */
    private String currentVersion = "v2";
    
    /**
     * Versões suportadas pela API.
     */
    private List<String> supportedVersions = List.of("v1", "v2");
    
    /**
     * Versões marcadas como deprecated e suas informações.
     */
    private Map<String, DeprecationInfo> deprecatedVersions = Map.of(
        "v1", new DeprecationInfo(
            "Esta versão será removida em 6 meses. Use /api/v2/votos",
            LocalDateTime.of(2025, 2, 13, 0, 0),
            "/api/v2/votos"
        )
    );
    
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new ApiVersioningInterceptor(this))
                .addPathPatterns("/api/**");
    }
    
    public String getCurrentVersion() {
        return currentVersion;
    }
    
    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
    
    public List<String> getSupportedVersions() {
        return supportedVersions;
    }
    
    public void setSupportedVersions(List<String> supportedVersions) {
        this.supportedVersions = supportedVersions;
    }
    
    public Map<String, DeprecationInfo> getDeprecatedVersions() {
        return deprecatedVersions;
    }
    
    public void setDeprecatedVersions(Map<String, DeprecationInfo> deprecatedVersions) {
        this.deprecatedVersions = deprecatedVersions;
    }
    
    /**
     * Verifica se uma versão está deprecada.
     */
    public boolean isVersionDeprecated(String version) {
        return deprecatedVersions.containsKey(version);
    }
    
    /**
     * Obtém informações de deprecação de uma versão.
     */
    public DeprecationInfo getDeprecationInfo(String version) {
        return deprecatedVersions.get(version);
    }
    
    /**
     * Verifica se uma versão é suportada.
     */
    public boolean isVersionSupported(String version) {
        return supportedVersions.contains(version);
    }
    
    /**
     * Classe que armazena informações sobre deprecação de versões.
     */
    public static class DeprecationInfo {
        private final String message;
        private final LocalDateTime sunsetDate;
        private final String successorVersion;
        
        public DeprecationInfo(String message, LocalDateTime sunsetDate, String successorVersion) {
            this.message = message;
            this.sunsetDate = sunsetDate;
            this.successorVersion = successorVersion;
        }
        
        public String getMessage() {
            return message;
        }
        
        public LocalDateTime getSunsetDate() {
            return sunsetDate;
        }
        
        public String getSuccessorVersion() {
            return successorVersion;
        }
    }
}