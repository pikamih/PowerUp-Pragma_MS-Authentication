package co.com.pragma.model.role.gateways;

import co.com.pragma.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RoleRepository {

    Mono<Role> save(Role role);              // Guardar o actualizar rol
    Mono<Role> findByName(String name);      // Buscar rol por nombre (unicidad)
    Mono<Role> findById(UUID id);            // Buscar rol por ID
    Flux<Role> findAll();                     // Listar todos los roles
    Mono<Void> deleteById(UUID id);          // Eliminar por ID

}
