package org.arquitectura.reelclipsv2.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ReelClips API")
                        .version("1.0.0")
                        .description("API REST de ReelClips — Plataforma de videos cortos e interacciones. " +
                                "Proyecto académico — Diseño y Arquitectura de Software, Universidad de La Sabana.")
                        .contact(new Contact()
                                .name("Equipo ReelClips")
                                .email("eduardmesa@unisabana.edu.co")))
                .tags(List.of(
                        new Tag().name("Usuarios").description("Registro, autenticación y gestión de perfiles"),
                        new Tag().name("Reels").description("Publicación, edición y visualización de videos"),
                        new Tag().name("Categorías").description("Gestión de categorías temáticas"),
                        new Tag().name("Interacciones").description("Likes y comentarios sobre reels"),
                        new Tag().name("Feed").description("Feed de contenido paginado y filtrado"),
                        new Tag().name("Chat").description("Conversaciones y mensajes directos")
                ));
    }
}
