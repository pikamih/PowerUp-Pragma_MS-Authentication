package co.com.pragma.config;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.role.RoleUseCase;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleUseCase roleUseCase;
    private final UserUseCase userUseCase;
    private final TransactionalOperator txOperator;

    private final String adminEmail;
    private final String adminPassword;

    public DataInitializer(RoleUseCase roleUseCase, UserUseCase userUseCase, TransactionalOperator txOperator, @Value("${app.initial.admin.email}") String adminEmail, @Value("${app.initial.admin.password}") String adminPassword) {
        this.roleUseCase = roleUseCase;
        this.userUseCase = userUseCase;
        this.txOperator = txOperator;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info(">>> Ejecutando DataInitializer usando UseCases...");
        seedRolesAndAdmin()
                .doOnSuccess(v -> log.info(">>> DataInitializer finalizado correctamente"))
                .doOnError(e -> log.error(">>> Error en DataInitializer", e))
                .block(); // Bloquea solo durante inicialización
    }

    private Mono<Void> seedRolesAndAdmin() {
        List<String> baseRoles = List.of("ADMIN", "ASESOR", "CLIENTE");

        // 1. Crear roles usando RoleUseCase
        Mono<Void> createRoles = Flux.fromIterable(baseRoles)
                .flatMap(roleName -> roleUseCase.createRole(
                                new Role(null, roleName, roleName + " role")
                        ).doOnSuccess(r -> log.info("Rol creado: {}", roleName))
                        .onErrorResume(ex -> {
                            log.warn("No se pudo crear rol {}: {}", roleName, ex.getMessage());
                            return Mono.empty();
                        }))
                .then();

        // 2. Crear usuario admin usando UserUseCase
        Mono<Void> createAdmin = roleUseCase.listAllRoles()
                .filter(r -> r.getName().equalsIgnoreCase("ADMIN"))
                .next() // obtenemos el primer rol ADMIN
                .flatMap(adminRole -> {
                    User adminUser = User.builder()
                            .id(null)
                            .firstName("System")
                            .lastName("Administrator")
                            .documentId("00000000")
                            .email(adminEmail)
                            .password(adminPassword) // el UseCase se encargará de hashear
                            .baseSalary(0.0)
                            .role(adminRole)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return userUseCase.createUser(adminUser)
                            .doOnSuccess(u -> log.info("Usuario admin creado: {}", adminEmail))
                            .onErrorResume(ex -> {
                                log.warn("No se pudo crear admin (quizá ya existe): {}", ex.getMessage());
                                return Mono.empty();
                            });
                })
                .then(Mono.fromRunnable(() -> log.error("Rol ADMIN no encontrado, imposible crear admin")));


        return txOperator.execute(status -> createRoles.then(createAdmin)).then();
    }
}
