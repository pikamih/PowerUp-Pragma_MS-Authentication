package co.com.pragma.api.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth", scheme = "bearer", type = SecuritySchemeType.HTTP, description = "JWT Bearer authentication", bearerFormat = "JWT")
public class OpenApiConfig {

    @Bean
    public OpenAPI crediyaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CrediYa API")
                        .description("API de gestión de solicitudes de préstamos personales")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Equipo CrediYa")
                                .email("soporte@crediya.com")
                                .url("https://www.crediya.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación CrediYa")
                        .url("https://docs.crediya.com"));
    }
}

