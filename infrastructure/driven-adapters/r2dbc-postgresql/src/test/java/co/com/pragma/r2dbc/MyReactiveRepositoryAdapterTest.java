package co.com.pragma.r2dbc;

import co.com.pragma.model.role.Role;
import co.com.pragma.r2dbc.adapter.RoleReactiveRepositoryAdapter;
import co.com.pragma.r2dbc.entity.RoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleReactiveRepositoryAdapterTest {

    @InjectMocks
    RoleReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    RoleReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        UUID id = UUID.randomUUID();
        RoleEntity entity = new RoleEntity(id, "Admin", "Administrador");
        Role domain = new Role(id, "Admin", "Administrador");

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Role.class)).thenReturn(domain);

        Mono<Role> result = repositoryAdapter.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getName().equals("Admin"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        RoleEntity entity = new RoleEntity(UUID.randomUUID(), "Admin", "Administrador");
        Role domain = new Role(entity.getId(), "Admin", "Administrador");

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Role.class)).thenReturn(domain);

        Flux<Role> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getName().equals("Admin"))
                .verifyComplete();
    }
}
