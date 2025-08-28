package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.UUID;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    /**
     * Crea un nuevo usuario validando los criterios de aceptación
     */
    public Mono<User> createUser(User user) {

        // Validaciones de campos obligatorios
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return Mono.error(new IllegalArgumentException("First name is required"));
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Last name is required"));
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Email is required"));
        }
        if (user.getBaseSalary() == null) {
            return Mono.error(new IllegalArgumentException("Base salary is required"));
        }

        // Validación de rango de salario
        if (user.getBaseSalary() < 0 || user.getBaseSalary() > 15000000) {
            return Mono.error(new IllegalArgumentException("Base salary must be between 0 and 15,000,000"));
        }

        // Validación de formato de email
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            return Mono.error(new IllegalArgumentException("Email format is invalid"));
        }

        // Validación de email único
        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Email already exists"));
                    }

                    // Asignar ID y timestamps
                    User userToSave = user.toBuilder()
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return userRepository.save(userToSave);
                });
    }

    // Otros métodos CRUD
    public Mono<User> updateUser(UUID userId, User user) {
        return userRepository.findById(userId)
                .flatMap(existingUser -> {

                    // Comparamos si hay cambios
                    if (existingUser.equals(user)) {
                        // No hay cambios, devolvemos mensaje personalizado
                        return Mono.error(new IllegalStateException("El usuario ya está actualizado"));
                    }

                    // Validaciones de campos obligatorios
                    if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("First name is required"));
                    }
                    if (user.getLastName() == null || user.getLastName().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Last name is required"));
                    }
                    if (user.getEmail() == null || user.getEmail().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Email is required"));
                    }
                    if (user.getBaseSalary() == null) {
                        return Mono.error(new IllegalArgumentException("Base salary is required"));
                    }

                    // Validación de formato de email
                    Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
                    if (!emailPattern.matcher(user.getEmail()).matches()) {
                        return Mono.error(new IllegalArgumentException("Email format is invalid"));
                    }

                    // Asignar timestamps y mantener ID
                    user.setId(existingUser.getId());
                    user.setCreatedAt(existingUser.getCreatedAt());
                    user.setUpdatedAt(LocalDateTime.now());

                    // Llamada al repositorio para hacer UPDATE real
                    return userRepository.update(user);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario no encontrado")));
    }




    public Mono<Void> deleteUser(UUID id) {
        return userRepository.findById(id)
                .flatMap(user -> userRepository.delete(id))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario no existe")));
    }


    public Mono<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
}
