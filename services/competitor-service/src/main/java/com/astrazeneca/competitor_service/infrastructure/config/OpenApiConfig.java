package com.astrazeneca.competitor_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger Configuration
 * Infrastructure Layer - Configuration
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI competitorServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("AstraZeneca");
        contact.setEmail("support@astrazeneca.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Competitor Service API")
                .version("1.0.0")
                .description("REST API for managing pharmaceutical competitors and their clinical trials. " +
                        "Built with Hexagonal Architecture (Ports and Adapters pattern).")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}

