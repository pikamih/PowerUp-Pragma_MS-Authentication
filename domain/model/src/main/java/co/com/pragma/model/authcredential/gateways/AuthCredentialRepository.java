package co.com.pragma.model.authcredential.gateways;

import co.com.pragma.model.authcredential.AuthCredential;
import co.com.pragma.model.authtoken.AuthToken;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AuthCredentialRepository {
    Mono<AuthToken> authenticate(AuthCredential authCredential);
}
