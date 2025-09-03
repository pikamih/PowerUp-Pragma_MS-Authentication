package co.com.pragma.usecase.user;

import co.com.pragma.model.authcredential.gateways.PasswordHasher;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;

    public Mono<User> createUser(User user) {

        // Validaciones
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_FIRST_NAME_REQUIRED, new Object[]{}));
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_LAST_NAME_REQUIRED, new Object[]{}));
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_EMAIL_REQUIRED, new Object[]{}));
        }
        if (user.getDocumentId() == null || user.getDocumentId().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_DOCUMENT_ID_REQUIRED, new Object[]{}));
        }
        if (user.getBaseSalary() == null) {
            return Mono.error(new BusinessException(MessageCode.USER_BASE_SALARY_REQUIRED, new Object[]{}));
        }
        if (user.getBaseSalary() < 0 || user.getBaseSalary() > 15_000_000) {
            return Mono.error(new BusinessException(MessageCode.USER_BASE_SALARY_INVALID, new Object[]{}));
        }

        // Validación de formato de email
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            return Mono.error(new BusinessException(MessageCode.USER_EMAIL_INVALID, new Object[]{}));
        }

        // Validar que el rol exista
        if (user.getRole() == null || user.getRole().getName() == null || user.getRole().getName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_ROLE_REQUIRED, new Object[]{}));
        }


        String roleName = user.getRole().getName().toUpperCase();

        return roleRepository.findByName(roleName)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.USER_ROLE_NOT_FOUND, new Object[]{})))
                .flatMap(role ->
                        userRepository.existsByEmail(user.getEmail())
                                .flatMap(emailExists -> {
                                    if (emailExists) {
                                        return Mono.error(new BusinessException(MessageCode.USER_EMAIL_ALREADY_EXISTS, new Object[]{}));
                                    }
                                    return userRepository.existsByDocumentId(user.getDocumentId());
                                })
                                .flatMap(docExists -> {
                                    if (docExists) {
                                        return Mono.error(new BusinessException(MessageCode.USER_DOCUMENT_ID_ALREADY_EXISTS, new Object[]{}));
                                    }

                                    System.out.println("passwordd: " + user);
                                    String hashedPassword = passwordHasher.hash(user.getPassword());
                                    user.setPassword(hashedPassword);
                                    // Asignar el rol validado al usuario antes de guardar
                                    user.setRole(role);
                                    return userRepository.save(user);
                                })
                );

    }

    public Mono<User> getUserById(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.USER_NOT_FOUND_BY_ID, new Object[]{id})));
    }

    public Flux<User> listUsers() {
        return userRepository.findAll();
    }

    public Mono<User> updateUser(UUID id, User user) {
        // Validaciones
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_FIRST_NAME_REQUIRED, new Object[]{}));
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_LAST_NAME_REQUIRED, new Object[]{}));
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_EMAIL_REQUIRED, new Object[]{}));
        }
        if (user.getDocumentId() == null || user.getDocumentId().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_DOCUMENT_ID_REQUIRED, new Object[]{}));
        }
        if (user.getBaseSalary() == null) {
            return Mono.error(new BusinessException(MessageCode.USER_BASE_SALARY_REQUIRED, new Object[]{}));
        }
        if (user.getBaseSalary() < 0 || user.getBaseSalary() > 15_000_000) {
            return Mono.error(new BusinessException(MessageCode.USER_BASE_SALARY_INVALID, new Object[]{}));
        }

        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            return Mono.error(new BusinessException(MessageCode.USER_EMAIL_INVALID, new Object[]{}));
        }

        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.USER_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing ->
                        userRepository.existsByEmail(user.getEmail())
                                .flatMap(duplicate -> {
                                    if (!duplicate) {
                                        return Mono.error(new BusinessException(MessageCode.USER_EMAIL_ALREADY_EXISTS, new Object[]{}));
                                    }
                                    return userRepository.existsByDocumentId(user.getDocumentId())
                                            .flatMap(dupDoc -> {
                                                if (dupDoc) {
                                                    return Mono.error(new BusinessException(MessageCode.USER_DOCUMENT_ID_ALREADY_EXISTS, new Object[]{}));
                                                }
                                                // Solo actualizamos lo permitido
                                                existing.setFirstName(user.getFirstName());
                                                existing.setLastName(user.getLastName());
                                                existing.setEmail(user.getEmail());
                                                existing.setDocumentId(user.getDocumentId());
                                                existing.setBaseSalary(user.getBaseSalary());
                                                existing.setUpdatedAt(LocalDateTime.now()); // se actualiza aquí
                                                return userRepository.save(existing);
                                            });
                                })
                );
    }

    public Mono<Void> deleteUser(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.USER_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing -> userRepository.delete(id));
    }
}
