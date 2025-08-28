package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UserRepository {

    /**
     * Guarda un nuevo usuario en el sistema.
     * El UseCase será responsable de validar los criterios de aceptación.
     * @param user Entidad User a registrar
     * @return Mono con el usuario creado (incluye id y timestamps)
     */
    Mono<User> save(User user);

    /**
     * Actualiza un usuario existente.
     * @param user Entidad User con los cambios
     * @return Mono con el usuario actualizado
     */
    Mono<User> update(User user);

    /**
     * Elimina un usuario por su ID
     * @param id Identificador del usuario
     * @return Mono vacío al completar la eliminación
     */
    Mono<Void> delete(UUID id);

    /**
     * Verifica si un correo electrónico ya está registrado.
     * @param email Correo a validar
     * @return Mono con true si el email ya existe, false si no
     */
    Mono<Boolean> existsByEmail(String email);

    /**
     * Busca un usuario por su ID
     * @param id Identificador del usuario
     * @return Mono con el usuario encontrado o vacío
     */
    Mono<User> findById(UUID id);

    /**
     * Lista todos los usuarios registrados
     * @return Flux con todos los usuarios
     */
    Flux<User> findAll();
}
