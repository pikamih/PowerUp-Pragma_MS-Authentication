package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.UserReactiveRepository;
import co.com.pragma.r2dbc.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserReactiveRepositoryAdapter implements UserRepository {

    private final UserReactiveRepository repository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<User> save(User user) {
        /*return repository.save(UserEntityMapper.toEntity(user))
                .map(UserEntityMapper::toDomain);*/
        return transactionalOperator
                .execute(status -> repository.save(UserEntityMapper.toEntity(user)))
                .map(UserEntityMapper::toDomain)
                .single();
    }

    @Override
    public Mono<User> update(User user) {
        /*return repository.save(UserEntityMapper.toEntity(user))
                .map(UserEntityMapper::toDomain);*/
        return transactionalOperator
                .execute(status -> repository.save(UserEntityMapper.toEntity(user)))
                .map(UserEntityMapper::toDomain)
                .single();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return repository.findById(id)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Flux<User> findAll() {
        return repository.findAll()
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.findAll()
                .any(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
