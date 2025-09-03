package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.RoleRequestDto;
import co.com.pragma.api.dto.response.RoleResponseDto;
import co.com.pragma.api.mapper.RoleWebMapper;
import co.com.pragma.usecase.role.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleUseCase roleUseCase;
    private final RoleWebMapper roleWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RoleResponseDto> createRole(@Valid @RequestBody RoleRequestDto dto) {
        return roleUseCase.createRole(roleWebMapper.toDomain(dto))
                .map(roleWebMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<RoleResponseDto> findByIdRole(@PathVariable("id") String id) {
        UUID roleId;
        try {
            roleId = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }
        return roleUseCase.findByIdRole(roleId)
                .map(roleWebMapper::toResponse);
    }

    @GetMapping
    public Flux<RoleResponseDto> listAllRoles() {
        return roleUseCase.listAllRoles()
                .map(roleWebMapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<RoleResponseDto> updateRole(@PathVariable("id") String id, @RequestBody RoleRequestDto dto) {
        UUID roleId;
        try {
            roleId = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }
        return roleUseCase.updateRole(roleId, roleWebMapper.toDomain(dto))
                .map(roleWebMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<String>> deleteRole(@PathVariable("id") String id) {
        UUID roleId;
        try {
            roleId = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }
        return roleUseCase.deleteRole(roleId)
                .then(Mono.just(ResponseEntity.ok("El rol se borro exitosamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body(e.getMessage())));
    }
}
