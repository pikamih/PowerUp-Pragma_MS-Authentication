package co.com.pragma.api.handler;

import co.com.pragma.api.dto.request.AuthCredentialRequestDto;
import co.com.pragma.api.dto.response.AuthTokenResponseDto;
import co.com.pragma.api.mapper.AuthCredentialWebMapper;
import co.com.pragma.api.mapper.AuthTokenWebMapper;
import co.com.pragma.usecase.authcredential.AuthCredentialUseCase;
import co.com.pragma.usecase.authtoken.AuthTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthCredentialUseCase authCredentialUseCase;
    private final AuthTokenUseCase authTokenUseCase;
    private final AuthCredentialWebMapper authCredentialWebMapper;
    private final AuthTokenWebMapper authTokenWebMapper;

    // Para @RestController
    public Mono<AuthTokenResponseDto> login(AuthCredentialRequestDto requestDto) {
        var credential = authCredentialWebMapper.toDomain(requestDto);

        return authCredentialUseCase.validateCredentials(credential)
                .flatMap(user ->
                        authTokenUseCase.generateToken(
                                        credential,
                                        user.getRole().getName(),
                                        user.getFirstName() + " " + user.getLastName(),
                                user.getDocumentId()
                                )
                                .map(authTokenWebMapper::toResponse)
                );
    }

    // Para RouterFunctions / WebFlux
    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthCredentialRequestDto.class)
                .flatMap(this::login)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}
