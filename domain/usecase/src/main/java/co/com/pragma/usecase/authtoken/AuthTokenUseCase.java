package co.com.pragma.usecase.authtoken;

import co.com.pragma.model.authcredential.AuthCredential;
import co.com.pragma.model.authtoken.AuthToken;
import co.com.pragma.model.authtoken.gateways.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthTokenUseCase {

    private final AuthTokenRepository tokenGenerator;

    public Mono<AuthToken> generateToken(AuthCredential credential, String role, String name, String documentId) {
        AuthToken tokenDomain = AuthToken.builder()
                .email(credential.getEmail())
                .role(role)
                .name(name)
                .documentId(documentId)
                .build();

        return tokenGenerator.generateToken(tokenDomain);
    }

}
