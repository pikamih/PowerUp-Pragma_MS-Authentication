package co.com.pragma.r2dbc;

import co.com.pragma.model.role.Role;
import co.com.pragma.r2dbc.adapter.RoleReactiveRepositoryAdapter;
import co.com.pragma.r2dbc.entity.RoleEntity;
import co.com.pragma.r2dbc.mapper.RoleEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleReactiveRepositoryAdapterTest {

    private RoleReactiveRepository roleRepository;
    private RoleEntityMapper roleEntityMapper;
    private TransactionalOperator transactionalOperator;
    private RoleReactiveRepositoryAdapter repositoryAdapter;

    @BeforeEach
    void setup() {
        roleRepository = mock(RoleReactiveRepository.class);
        roleEntityMapper = mock(RoleEntityMapper.class);
        transactionalOperator = mock(TransactionalOperator.class);
        repositoryAdapter = new RoleReactiveRepositoryAdapter(roleRepository, transactionalOperator, roleEntityMapper);
    }

    @Test
    void testSave() {
        UUID id = UUID.randomUUID();
        Role domainRole = new Role(id, "Admin", "Administrador");
        RoleEntity entity = new RoleEntity(id, "Admin", "Administrador");

        when(roleEntityMapper.toEntity(domainRole)).thenReturn(entity);
        when(roleEntityMapper.toDomain(entity)).thenReturn(domainRole);

        when(roleRepository.save(entity)).thenReturn(Mono.just(entity));

        when(transactionalOperator.execute(any()))
                .thenAnswer(invocation -> {
                    var callback = invocation.getArgument(0, org.springframework.transaction.reactive.TransactionCallback.class);
                    // Convertimos a Flux para que no haya ClassCastException
                    return Flux.from(callback.doInTransaction(null));
                });

        StepVerifier.create(repositoryAdapter.save(domainRole))
                .expectNextMatches(r -> r.getName().equals("Admin") && r.getDescription().equals("Administrador"))
                .verifyComplete();
    }


    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Role domainRole = new Role(id, "Admin", "Administrador");
        RoleEntity entity = new RoleEntity(id, "Admin", "Administrador");

        when(roleRepository.findById(id)).thenReturn(Mono.just(entity));
        when(roleEntityMapper.toDomain(entity)).thenReturn(domainRole);

        StepVerifier.create(repositoryAdapter.findById(id))
                .expectNext(domainRole)
                .verifyComplete();
    }

    @Test
    void testFindByName() {
        String name = "Admin";
        UUID id = UUID.randomUUID();
        Role domainRole = new Role(id, name, "Administrador");
        RoleEntity entity = new RoleEntity(id, name, "Administrador");

        when(roleRepository.findByName(name)).thenReturn(Mono.just(entity));
        when(roleEntityMapper.toDomain(entity)).thenReturn(domainRole);

        StepVerifier.create(repositoryAdapter.findByName(name))
                .expectNext(domainRole)
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        RoleEntity e1 = new RoleEntity(id1, "Admin", "Administrador");
        RoleEntity e2 = new RoleEntity(id2, "User", "Usuario");
        Role r1 = new Role(id1, "Admin", "Administrador");
        Role r2 = new Role(id2, "User", "Usuario");

        when(roleRepository.findAll()).thenReturn(Flux.just(e1, e2));
        when(roleEntityMapper.toDomain(e1)).thenReturn(r1);
        when(roleEntityMapper.toDomain(e2)).thenReturn(r2);

        StepVerifier.create(repositoryAdapter.findAll())
                .expectNext(r1)
                .expectNext(r2)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();

        when(roleRepository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.deleteById(id))
                .verifyComplete();
    }
}
