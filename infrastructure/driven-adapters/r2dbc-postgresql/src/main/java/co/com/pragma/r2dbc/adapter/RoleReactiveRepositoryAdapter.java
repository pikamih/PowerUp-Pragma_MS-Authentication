package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.r2dbc.mapper.RoleEntityMapper;
import co.com.pragma.r2dbc.RoleReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleReactiveRepositoryAdapter implements RoleRepository {

    private final RoleReactiveRepository roleRepository;
    private final TransactionalOperator transactionalOperator;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Mono<Role> save(Role role) {
        return transactionalOperator
                .execute(status -> roleRepository.save(roleEntityMapper.toEntity(role)))
                .map(roleEntityMapper::toDomain)
                .single();
    }

    @Override
    public Mono<Role> findById(UUID id) {
        return roleRepository.findById(id)
                .map(roleEntityMapper::toDomain);
    }

    @Override
    public Mono<Role> findByName(String name) {
        return roleRepository.findByName(name)
                .map(roleEntityMapper::toDomain);
    }

    @Override
    public Flux<Role> findAll() {
        return roleRepository.findAll()
                .map(roleEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return roleRepository.deleteById(id);
    }
}
