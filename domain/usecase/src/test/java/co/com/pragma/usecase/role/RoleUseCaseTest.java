package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import co.com.pragma.usecase.mock.RoleMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleUseCaseTest {

    private RoleRepository roleRepository;
    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleUseCase = new RoleUseCase(roleRepository);
    }

    // --- CREATE ROLE ---
    @Test
    void createRole_success() {
        Role role = RoleMock.getDefault();

        when(roleRepository.findByName(role.getName())).thenReturn(Mono.empty());
        when(roleRepository.save(role)).thenReturn(Mono.just(role));

        StepVerifier.create(roleUseCase.createRole(role))
                .assertNext(saved -> {
                    assertThat(saved.getName()).isEqualTo(role.getName());
                    assertThat(saved.getDescription()).isEqualTo(role.getDescription());
                })
                .verifyComplete();
    }

    @Test
    void createRole_missingName() {
        Role invalidRole = Role.builder().name("").build();

        StepVerifier.create(roleUseCase.createRole(invalidRole))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(BusinessException.class);
                    assertThat(((BusinessException) error).getMessageCode()).isEqualTo(MessageCode.ROLE_NAME_REQUIRED);
                })
                .verify();
    }

    @Test
    void createRole_duplicateName() {
        Role role = RoleMock.getDefault();

        when(roleRepository.findByName(role.getName())).thenReturn(Mono.just(role));

        StepVerifier.create(roleUseCase.createRole(role))
                .expectError(BusinessException.class)
                .verify();
    }

    // --- FIND BY ID ---
    @Test
    void findByIdRole_success() {
        Role role = RoleMock.getDefault();

        when(roleRepository.findById(role.getId())).thenReturn(Mono.just(role));

        StepVerifier.create(roleUseCase.findByIdRole(role.getId()))
                .assertNext(found -> assertThat(found.getId()).isEqualTo(role.getId()))
                .verifyComplete();
    }

    @Test
    void findByIdRole_notFound() {
        UUID id = UUID.randomUUID();

        when(roleRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(roleUseCase.findByIdRole(id))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(BusinessException.class);
                    assertThat(((BusinessException) error).getMessageCode()).isEqualTo(MessageCode.ROLE_NOT_FOUND_BY_ID);
                })
                .verify();
    }

    // --- LIST ALL ---
    @Test
    void listAllRoles_success() {
        when(roleRepository.findAll()).thenReturn(Flux.just(RoleMock.getDefault(), RoleMock.getWithCustomName("USER")));

        StepVerifier.create(roleUseCase.listAllRoles())
                .expectNextCount(2)
                .verifyComplete();
    }

    // --- UPDATE ---
    @Test
    void updateRole_success() {
        UUID id = UUID.randomUUID();
        Role existing = RoleMock.getWithCustomId(id);
        Role update = Role.builder().name("NEW_ROLE").description("Updated desc").build();

        when(roleRepository.findById(id)).thenReturn(Mono.just(existing));
        when(roleRepository.findByName(update.getName())).thenReturn(Mono.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(Mono.just(existing.toBuilder()
                .name(update.getName())
                .description(update.getDescription())
                .build()));

        StepVerifier.create(roleUseCase.updateRole(id, update))
                .assertNext(updated -> {
                    assertThat(updated.getName()).isEqualTo("NEW_ROLE");
                    assertThat(updated.getDescription()).isEqualTo("Updated desc");
                })
                .verifyComplete();
    }

    @Test
    void updateRole_notFound() {
        UUID id = UUID.randomUUID();
        Role update = RoleMock.getWithCustomName("NEW");

        when(roleRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(roleUseCase.updateRole(id, update))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(BusinessException.class);
                    assertThat(((BusinessException) error).getMessageCode()).isEqualTo(MessageCode.ROLE_NOT_FOUND_BY_ID);
                })
                .verify();
    }

    @Test
    void updateRole_duplicateName() {
        UUID id = UUID.randomUUID();
        Role existing = RoleMock.getWithCustomId(id);
        Role duplicate = RoleMock.getWithCustomName("ADMIN"); // otro con mismo nombre

        when(roleRepository.findById(id)).thenReturn(Mono.just(existing));
        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(duplicate));

        StepVerifier.create(roleUseCase.updateRole(id, Role.builder().name("ADMIN").build()))
                .expectError(BusinessException.class)
                .verify();
    }

    // --- DELETE ---
    @Test
    void deleteRole_success() {
        UUID id = UUID.randomUUID();
        Role role = RoleMock.getWithCustomId(id);

        when(roleRepository.findById(id)).thenReturn(Mono.just(role));
        when(roleRepository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(roleUseCase.deleteRole(id))
                .verifyComplete();

        verify(roleRepository).deleteById(id);
    }

    @Test
    void deleteRole_notFound() {
        UUID id = UUID.randomUUID();

        when(roleRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(roleUseCase.deleteRole(id))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(BusinessException.class);
                    assertThat(((BusinessException) error).getMessageCode()).isEqualTo(MessageCode.ROLE_NOT_FOUND_BY_ID);
                })
                .verify();
    }
}
