package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.RoleRequestDto;
import co.com.pragma.api.dto.response.RoleResponseDto;
import co.com.pragma.api.mapper.RoleWebMapper;
import co.com.pragma.usecase.role.RoleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auths/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Operaciones relacionadas con la gestión de roles")
public class RoleController {

    private final RoleUseCase roleUseCase;
    private final RoleWebMapper roleWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear rol",
            description = "Permite crear un nuevo rol en la plataforma",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rol creado correctamente",
                            content = @Content(schema = @Schema(implementation = RoleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content())
            }
    )
    public Mono<RoleResponseDto> createRole(@Valid @RequestBody RoleRequestDto dto) {
        return roleUseCase.createRole(roleWebMapper.toDomain(dto))
                .map(roleWebMapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener rol por ID",
            description = "Devuelve un rol específico según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol encontrado",
                            content = @Content(schema = @Schema(implementation = RoleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "UUID inválido", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content())
            }
    )
    public Mono<RoleResponseDto> findByIdRole(@PathVariable("id") String id) {
        UUID roleId;
        try {
            roleId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }
        return roleUseCase.findByIdRole(roleId)
                .map(roleWebMapper::toResponse);
    }

    @GetMapping
    @Operation(
            summary = "Listar roles",
            description = "Devuelve la lista completa de roles registrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de roles",
                            content = @Content(schema = @Schema(implementation = RoleResponseDto.class)))
            }
    )
    public Flux<RoleResponseDto> listAllRoles() {
        return roleUseCase.listAllRoles()
                .map(roleWebMapper::toResponse);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar rol",
            description = "Permite actualizar los datos de un rol existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol actualizado",
                            content = @Content(schema = @Schema(implementation = RoleResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "UUID inválido o datos inválidos", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content())
            }
    )
    public Mono<RoleResponseDto> updateRole(@PathVariable("id") String id, @RequestBody RoleRequestDto dto) {
        UUID roleId;
        try {
            roleId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }
        return roleUseCase.updateRole(roleId, roleWebMapper.toDomain(dto))
                .map(roleWebMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar rol",
            description = "Permite eliminar un rol por su ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Rol eliminado correctamente"),
                    @ApiResponse(responseCode = "400", description = "UUID inválido", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content())
            }
    )
    public Mono<ResponseEntity<String>> deleteRole(@PathVariable("id") String id) {
        UUID roleId;
        try {
            roleId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }
        return roleUseCase.deleteRole(roleId)
                .then(Mono.just(ResponseEntity.ok("El rol se borró exitosamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body(e.getMessage())));
    }
}
