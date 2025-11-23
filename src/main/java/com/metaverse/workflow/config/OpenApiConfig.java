package com.metaverse.workflow.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;

// We'll use fully-qualified model classes below to avoid name clashes with the annotation types.

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Workflow API",
                version = "1.0",
                description = "Workflow API Documentation",
                contact = @Contact(
                        name = "Metaverse",
                        email = "support@metaverse.com"
                )
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "/workflow"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    // Expose a programmatic OpenAPI bean so we can inject build-time info from resources.
    @Bean
    public OpenAPI customOpenAPI(@Value("${build.time:unknown}") String buildTime) {
        // Build a description/version that includes the build timestamp.
        String buildNote = "(build: " + buildTime + ")";

        io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info()
                .title("Workflow API")
                .version("1.0 " + buildNote)
                .description("Workflow API Documentation " + buildNote)
                .contact(new io.swagger.v3.oas.models.info.Contact().name("Metaverse").email("support@metaverse.com"));

        return new OpenAPI().info(info);
    }
}