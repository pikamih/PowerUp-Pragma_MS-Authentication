package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.r2dbc.entity.RoleEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.mapper.RoleEntityMapper;
import co.com.pragma.r2dbc.RoleReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
@Primary
public class RoleReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Role, RoleEntity, UUID, RoleReactiveRepository>
        implements RoleRepository {

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, RoleEntityMapper::toRole);
    }

    @Override
    public Mono<Role> save(Role role) {
        return repository.save(RoleEntityMapper.fromRole(role))
                .map(RoleEntityMapper::toRole);
    }

    @Override
    public Mono<Role> findById(UUID id) {
        return repository.findById(id)
                .map(RoleEntityMapper::toRole);
    }

    @Override
    public Mono<Role> findByName(String name) {
        return repository.findByName(name)
                .map(RoleEntityMapper::toRole);
    }

    @Override
    public Flux<Role> findAll() {
        return repository.findAll()
                .map(RoleEntityMapper::toRole);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return repository.deleteById(id);
    }
}
