package co.com.pragma.api.handler;

import co.com.pragma.usecase.role.RoleUseCase;
import co.com.pragma.model.role.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.UUID;

@Component
public class RoleHandler {

    private final RoleUseCase roleUseCase;

    public RoleHandler(RoleUseCase roleUseCase) {
        this.roleUseCase = roleUseCase;
    }

    // Crear un rol
    public Mono<ServerResponse> createRole(ServerRequest request) {
        return request.bodyToMono(Role.class)
                .flatMap(roleUseCase::createRole)
                .flatMap(role -> ServerResponse
                        .created(URI.create("/api/v1/roles/" + role.getId()))
                        .bodyValue(role))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .bodyValue(e.getMessage()));
    }

    // Obtener un rol por ID
    public Mono<ServerResponse> getRoleById(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return roleUseCase.getRoleById(id)
                .flatMap(role -> ServerResponse.ok().bodyValue(role))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    // Listar todos los roles
    public Mono<ServerResponse> listRoles(ServerRequest request) {
        Flux<Role> roles = roleUseCase.listRoles();
        return ServerResponse.ok().body(roles, Role.class);
    }

    // Actualizar un rol
    public Mono<ServerResponse> updateRole(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return request.bodyToMono(Role.class)
                .flatMap(role -> roleUseCase.updateRole(id, role))
                .flatMap(updated -> ServerResponse.ok().bodyValue(updated))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .bodyValue(e.getMessage()));
    }

    // Eliminar un rol
    public Mono<ServerResponse> deleteRole(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return roleUseCase.deleteRole(id)
                .then(ServerResponse.noContent().build())
                .onErrorResume(e -> ServerResponse.badRequest()
                        .bodyValue(e.getMessage()));
    }
}
