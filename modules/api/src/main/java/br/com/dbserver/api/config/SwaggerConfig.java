package br.com.dbserver.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}