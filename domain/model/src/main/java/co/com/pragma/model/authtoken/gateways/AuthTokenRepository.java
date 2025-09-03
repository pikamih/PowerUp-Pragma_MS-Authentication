package co.com.pragma.model.authtoken.gateways;

import co.com.pragma.model.authtoken.AuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenRepository {

    Mono<AuthToken> generateToken(AuthToken authToken);
    Mono<AuthToken> validateToken(String token);
}
