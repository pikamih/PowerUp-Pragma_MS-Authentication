package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.RoleReactiveRepository;
import co.com.pragma.r2dbc.UserReactiveRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.mapper.RoleEntityMapper;
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
    private final RoleReactiveRepository roleReactiveRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final TransactionalOperator transactionalOperator;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Mono<User> save(User user) {
        return transactionalOperator
                .execute(status -> repository.save(userEntityMapper.toEntity(user)))
                .map(UserEntity::getId)
                .flatMap(repository::findById)
                .map(userEntityMapper::toDomain)
                .flatMap(savedUser ->
                        roleReactiveRepository.findById(savedUser.getRole().getId())
                                .map(role -> {
                                    savedUser.setRole(roleEntityMapper.toDomain(role));
                                    return savedUser;
                                })
                )
                .single();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return repository.findById(id)
                .flatMap(userEntity ->
                        roleReactiveRepository.findById(userEntity.getRoleId())
                                .map(roleEntity -> {
                                    User user = userEntityMapper.toDomain(userEntity);
                                    user.setRole(roleEntityMapper.toDomain(roleEntity)); // map RoleEntity → Role
                                    return user;
                                })
                );
    }



    @Override
    public Flux<User> findAll() {
        return repository.findAll()
                .flatMap(userEntity ->
                        roleReactiveRepository.findById(userEntity.getRoleId())
                                .map(roleEntity -> {
                                    User user = userEntityMapper.toDomain(userEntity);
                                    user.setRole(roleEntityMapper.toDomain(roleEntity)); // map RoleEntity → Role
                                    return user;
                                })
                );
    }



    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.findAll()
                .any(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public Mono<Boolean> existsByDocumentId(String documentId) {
        return repository.existsByDocumentId(documentId);
    }
}
