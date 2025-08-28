package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleRepository roleRepository;

    public Mono<Role> createRole(Role role) {
        if (role.getName() == null || role.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El nombre del rol es obligatorio"));
        }
        return roleRepository.findByName(role.getName())
                .flatMap(existing -> Mono.<Role>error(new IllegalArgumentException("El rol ya existe")))
                .switchIfEmpty(Mono.defer(() ->
                    roleRepository.save(role)
                ));
    }

    public Mono<Role> getRoleById(UUID id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "El rol con id " + id + " no existe"))); //  mensaje si no existe

    }

    public Flux<Role> listRoles() {
        return roleRepository.findAll();
    }

    public Mono<Role> updateRole(UUID id, Role role) {
        if (role.getName() == null || role.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El nombre del rol es obligatorio"));
        }

        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "El rol con id " + id + " no existe"
                )))
                .flatMap(existing ->
                        roleRepository.findByName(role.getName())
                                .flatMap(duplicate -> {
                                    if (!duplicate.getId().equals(id)) {
                                        return Mono.error(new IllegalArgumentException("El nombre del rol ya existe"));
                                    }
                                    return Mono.just(duplicate);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    existing.setName(role.getName());
                                    existing.setDescription(role.getDescription());
                                    return roleRepository.save(existing);
                                }))
                );
    }



    public Mono<Void> deleteRole(UUID id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "El rol con id " + id + " no existe.")))
                .flatMap(existing -> roleRepository.deleteById(id));
    }

}
