package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.UserRequestDto;
import co.com.pragma.api.dto.response.UserResponseDto;
import co.com.pragma.api.mapper.UserWebMapper;
import co.com.pragma.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserWebMapper userWebMapper;

    @PreAuthorize("hasAuthority('ADMIN','ASESOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear usuario",
            description = "Permite crear un nuevo usuario en la plataforma",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public Mono<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        return userUseCase.createUser(userWebMapper.toDomain(dto))
                .map(userWebMapper::toResponse);
    }

    @GetMapping
    @Operation(
            summary = "Listar usuarios",
            description = "Devuelve la lista completa de usuarios registrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios",
                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public Flux<UserResponseDto> getAllUsers() {
        return userUseCase.listUsers()
                .map(userWebMapper::toResponse);
    }

    @GetMapping("/{documentId}")
    @Operation(
            summary = "Obtener usuario por documentId",
            description = "Devuelve un usuario específico según su documentId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "UUID inválido", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public Mono<UserResponseDto> getUserByDocumentId(@PathVariable("documentId") String documentId) {
        return userUseCase.getUserByDocumentId(documentId)
                .map(userWebMapper::toResponse);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Permite actualizar los datos de un usuario existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "UUID inválido o datos inválidos", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public Mono<UserResponseDto> updateUser(@PathVariable("id") String id,
                                            @RequestBody UserRequestDto dto) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format"));
        }
        return userUseCase.updateUser(userId, userWebMapper.toDomain(dto))
                .map(userWebMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar usuario",
            description = "Permite eliminar un usuario por su ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
                    @ApiResponse(responseCode = "400", description = "UUID inválido", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable("id") String id) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.badRequest().body("Formato de UUID inválido"));
        }

        return userUseCase.deleteUser(userId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
