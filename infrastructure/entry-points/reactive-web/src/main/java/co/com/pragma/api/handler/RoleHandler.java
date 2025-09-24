package co.com.pragma.api.handler;

import co.com.pragma.api.dto.request.RoleRequestDto;
import co.com.pragma.api.mapper.RoleWebMapper;
import co.com.pragma.usecase.role.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class RoleHandler {

    private final RoleUseCase roleUseCase;
    private final RoleWebMapper roleWebMapper;

    public Mono<ServerResponse> createRole(ServerRequest request) {
        return request.bodyToMono(RoleRequestDto.class).flatMap(dto -> {
            //Convertir a dominio
            var roleDomain = roleWebMapper.toDomain(dto);
            //Lamar al UseCase
            return roleUseCase.createRole(roleDomain);
        }).flatMap(savedRole -> ServerResponse.created(URI.create("/api/v1/roles/"))
                .contentType(APPLICATION_JSON).bodyValue(roleWebMapper.toResponse(savedRole)));
    }

    // Obtener un rol por ID
    public Mono<ServerResponse> getRoleById(ServerRequest request) {
        try {
            UUID roleId = UUID.fromString(request.pathVariable("id"));
            return roleUseCase.findByIdRole(roleId).flatMap(role -> ServerResponse.ok()
                    .contentType(APPLICATION_JSON).bodyValue(roleWebMapper.toResponse(role)));
        }catch (IllegalArgumentException e){
            return ServerResponse.badRequest().bodyValue("Invalid UUID format: " + request.pathVariable("id"));
        }
    }

    // Listar todos los roles
    public Mono<ServerResponse> listRoles(ServerRequest request) {
        return roleUseCase.listAllRoles().collectList().flatMap(role -> ServerResponse.ok()
                .contentType(APPLICATION_JSON).bodyValue(role.stream().map(roleWebMapper::toResponse).toList()));

    }

    // Actualizar un rol
    public Mono<ServerResponse> updateRole(ServerRequest request) {
        try {
            UUID roleId = UUID.fromString(request.pathVariable("id"));
            return request.bodyToMono(RoleRequestDto.class).flatMap(dto -> {
                var roleDomain = roleWebMapper.toDomain(dto);
                return roleUseCase.updateRole(roleId, roleDomain);
            }).flatMap(updateRole -> ServerResponse.ok()
                    .contentType(APPLICATION_JSON).bodyValue(roleWebMapper.toResponse(updateRole)));
        } catch (IllegalArgumentException e){
            return ServerResponse.badRequest().bodyValue("Invalid UUID format: " + request.pathVariable("id"));
        }
    }

    // Eliminar un rol
    public Mono<ServerResponse> deleteRole(ServerRequest request) {
        try {
            UUID roleId = UUID.fromString(request.pathVariable("id"));
            return roleUseCase.deleteRole(roleId).then(ServerResponse.noContent().build());
        }catch (IllegalArgumentException e){
            return ServerResponse.badRequest().bodyValue("Invalid UUID format: " + request.pathVariable("id"));
        }
    }
}
