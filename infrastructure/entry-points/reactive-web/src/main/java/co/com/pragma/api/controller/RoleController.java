package co.com.pragma.api.controller;

import co.com.pragma.model.role.Role;
import co.com.pragma.usecase.role.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    public Mono<ResponseEntity<Role>> createRole(@Valid @RequestBody Role role) {
        return roleUseCase.createRole(role)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r));
    }



    @GetMapping("/{id}")
    public Mono<ResponseEntity<Role>> getRole(@PathVariable("id") UUID id) {
        return roleUseCase.getRoleById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<Role> listRoles() {
        return roleUseCase.listRoles();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Role>> updateRole(@PathVariable("id") UUID id, @RequestBody Role role) {
        role.setId(id); // 🔹 Aseguramos que el Role tenga el ID correcto
        return roleUseCase.updateRole(id, role)
                .map(ResponseEntity::ok);
    }



    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRole(@PathVariable("id") UUID id) {
        return roleUseCase.deleteRole(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
