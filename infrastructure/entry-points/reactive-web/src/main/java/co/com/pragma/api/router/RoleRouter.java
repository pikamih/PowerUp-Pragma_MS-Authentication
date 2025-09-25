package co.com.pragma.api.router;

import co.com.pragma.api.handler.RoleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.RequestPredicates;

@Configuration
public class RoleRouter {

    @Bean
    public RouterFunction<?> route(RoleHandler handler) {
        return RouterFunctions
                // Crear un rol
                .route(RequestPredicates.POST("/api/v1/auths/roles"), handler::createRole)
                // Obtener un rol por ID
                .andRoute(RequestPredicates.GET("/api/v1/auths/roles/{id}"), handler::getRoleById)
                // Listar todos los roles
                .andRoute(RequestPredicates.GET("/api/v1/auths/roles"), handler::listRoles)
                // Actualizar un rol
                .andRoute(RequestPredicates.PUT("/api/v1/auths/roles/{id}"), handler::updateRole)
                // Eliminar un rol
                .andRoute(RequestPredicates.DELETE("/api/v1/auths/roles/{id}"), handler::deleteRole);
    }
}
