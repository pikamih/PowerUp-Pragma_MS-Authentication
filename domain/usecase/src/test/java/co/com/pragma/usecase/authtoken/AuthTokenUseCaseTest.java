package co.com.pragma.usecase.authtoken;

import co.com.pragma.model.authcredential.AuthCredential;
import co.com.pragma.model.authtoken.AuthToken;
import co.com.pragma.model.authtoken.gateways.AuthTokenRepository;
import co.com.pragma.usecase.mock.AuthCredentialMock;
import co.com.pragma.usecase.mock.AuthTokenMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthTokenUseCaseTest {

    private AuthTokenRepository tokenRepository;
    private AuthTokenUseCase useCase;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(AuthTokenRepository.class);
        useCase = new AuthTokenUseCase(tokenRepository);
    }

    @Test
    void generateToken_success() {
        // Arrange
        AuthCredential credential = AuthCredentialMock.getDefault();
        String role = "ADMIN";
        String name = "John Doe";
        String documentId = "12345678";

        AuthToken expectedToken = AuthTokenMock.getDefault();

        when(tokenRepository.generateToken(any(AuthToken.class)))
                .thenReturn(Mono.just(expectedToken));

        // Act
        Mono<AuthToken> result = useCase.generateToken(credential, role, name, documentId);

        // Assert
        StepVerifier.create(result)
                .assertNext(token -> {
                    assertThat(token.getEmail()).isEqualTo(expectedToken.getEmail());
                    assertThat(token.getRole()).isEqualTo(expectedToken.getRole());
                    assertThat(token.getName()).isEqualTo(expectedToken.getName());
                    assertThat(token.getDocumentId()).isEqualTo(expectedToken.getDocumentId());
                    assertThat(token.getToken()).isEqualTo(expectedToken.getToken());
                    assertThat(token.getExpiresAt()).isEqualTo(expectedToken.getExpiresAt());
                })
                .verifyComplete();

        // Además verificamos que el repo fue llamado con el AuthToken esperado
        ArgumentCaptor<AuthToken> captor = ArgumentCaptor.forClass(AuthToken.class);
        verify(tokenRepository).generateToken(captor.capture());

        AuthToken captured = captor.getValue();
        assertThat(captured.getEmail()).isEqualTo(credential.getEmail());
        assertThat(captured.getRole()).isEqualTo(role);
        assertThat(captured.getName()).isEqualTo(name);
        assertThat(captured.getDocumentId()).isEqualTo(documentId);
        assertThat(captured.getToken()).isNull(); // se genera en el repo
    }
}
