package co.com.pragma.api.handler;

import co.com.pragma.api.dto.request.UserRequestDto;
import co.com.pragma.api.dto.response.UserResponseDto;
import co.com.pragma.api.mapper.UserWebMapper;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;

    /**
     * Crear un nuevo usuario
     */
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDto.class)
                .flatMap(dto -> {
                    // Convertir a dominio
                    var userDomain = UserWebMapper.toDomain(dto);
                    // Llamar al UseCase
                    return userUseCase.createUser(userDomain);
                })
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(UserWebMapper.toResponse(savedUser))
                );
    }

    /**
     * Obtener usuario por ID
     */
    public Mono<ServerResponse> getUserById(ServerRequest request) {
        try {
            UUID userId = UUID.fromString(request.pathVariable("id"));
            return userUseCase.getUserById(userId)
                    .flatMap(user -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(UserWebMapper.toResponse(user))
                    );
        } catch (IllegalArgumentException e) {
            return ServerResponse.badRequest()
                    .bodyValue("Invalid UUID format: " + request.pathVariable("id"));
        }
    }


    /**
     * Listar todos los usuarios
     */
    public Mono<ServerResponse> listUsers(ServerRequest request) {
        return userUseCase.getAllUsers()
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(
                                users.stream()
                                        .map(UserWebMapper::toResponse)
                                        .toList()
                        )
                );
    }

    /**
     * Actualizar usuario por ID
     */
    public Mono<ServerResponse> updateUser(ServerRequest request) {
        try {
            UUID userId = UUID.fromString(request.pathVariable("id"));
            return request.bodyToMono(UserRequestDto.class)
                    .flatMap(dto -> {
                        var userDomain = UserWebMapper.toDomain(dto);
                        return userUseCase.updateUser(userId, userDomain);
                    })
                    .flatMap(updatedUser -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(UserWebMapper.toResponse(updatedUser))
                    );
        } catch (IllegalArgumentException e) {
            return ServerResponse.badRequest()
                    .bodyValue("Invalid UUID format: " + request.pathVariable("id"));
        }
    }


    /**
     * Eliminar usuario por ID
     */
    /**
     * Eliminar usuario por ID
     */
    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        try {
            UUID userId = UUID.fromString(request.pathVariable("id"));
            return userUseCase.deleteUser(userId)
                    .then(ServerResponse.noContent().build());
        } catch (IllegalArgumentException e) {
            return ServerResponse.badRequest()
                    .bodyValue("Invalid UUID format: " + request.pathVariable("id"));
        }
    }

}
