package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UserRepository {

    Mono<User> save(User user);
    Mono<Void> delete(UUID id);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByDocumentId(String documentId);
    Mono<User> findById(UUID id);
    Flux<User> findAll();
    Mono<User> findByEmail(String email);
}
