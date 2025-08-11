package br.com.dbserver.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI setDocumentationHeaderInformation() {
        return new OpenAPI()
            .info(new Info()
                .title("API RESTful - Urna Eletrônica")
                .version("1.0.0")
                .description("Desafio proposto pela empresa DB Server para o desenvolvimento de uma aplicação de Urna Eletrônica.")
                .contact(new Contact()
                    .name("Willian Sant' Anna")
                    .url("https://www.linkedin.com/in/wssantanna")
                )
            );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void tryOpenInDefaultBrowser() {
        try {
            System.out.println("Abrindo interface Swagger UI no seu navegador padrão...");
            
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080/swagger-ui.html"));
            }
            System.out.println("Disponível no endereço http://localhost:8080/swagger-ui.html");
        } catch (Exception e) {
            System.err.println("Não foi possível abrir o navegador automaticamente. Por favor, acesse o endereço http://localhost:8080/swagger-ui.html");
        }
    }
}