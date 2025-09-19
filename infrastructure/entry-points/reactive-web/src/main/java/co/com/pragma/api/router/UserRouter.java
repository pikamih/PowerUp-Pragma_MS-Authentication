package co.com.pragma.api.router;

import co.com.pragma.api.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route(POST("//api/v1/users"), handler::createUser)
                .andRoute(GET("/api/v1/users/{id}"), handler::getUserByDocumentId)
                .andRoute(GET("/api/v1/users"), handler::listUsers)
                .andRoute(PUT("//api/v1/users/{id}"), handler::updateUser)
                .andRoute(DELETE("/api/v1/users/{id}"), handler::deleteUser);
    }
}
