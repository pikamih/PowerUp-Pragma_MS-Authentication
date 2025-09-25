package co.com.pragma.usecase.authcredential;

import co.com.pragma.model.authcredential.AuthCredential;
import co.com.pragma.model.authcredential.gateways.PasswordHasher;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.mock.AuthCredentialMock;
import co.com.pragma.usecase.mock.RoleMock;
import co.com.pragma.usecase.mock.UserMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AuthCredentialUseCaseTest {

    private UserRepository userRepository;
    private PasswordHasher passwordHasher;
    private RoleRepository roleRepository;
    private AuthCredentialUseCase useCase;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        roleRepository = mock(RoleRepository.class);
        useCase = new AuthCredentialUseCase(userRepository, passwordHasher, roleRepository);
    }

    @Test
    void validateCredentials_success() {
        User user = UserMock.getDefault();
        AuthCredential credential = AuthCredentialMock.getDefault();

        when(userRepository.findByEmail(credential.getEmail())).thenReturn(Mono.just(user));
        when(passwordHasher.verify(credential.getPassword(), user.getPassword())).thenReturn(true);
        when(roleRepository.findById(user.getRole().getId())).thenReturn(Mono.just(RoleMock.getDefault()));

        StepVerifier.create(useCase.validateCredentials(credential))
                .expectNextMatches(u -> u.getEmail().equals(user.getEmail()) &&
                        u.getRole().getName().equals("ADMIN"))
                .verifyComplete();

        verify(userRepository).findByEmail(credential.getEmail());
        verify(passwordHasher).verify(credential.getPassword(), user.getPassword());
        verify(roleRepository).findById(user.getRole().getId());
    }

    @Test
    void validateCredentials_emailEmpty() {
        AuthCredential credential = AuthCredentialMock.getWithCustomEmail("");

        StepVerifier.create(useCase.validateCredentials(credential))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode().name().equals("USER_EMAIL_REQUIRED");
                })
                .verify();
    }

    @Test
    void validateCredentials_passwordEmpty() {
        AuthCredential credential = AuthCredentialMock.getWithCustomPassword("");

        StepVerifier.create(useCase.validateCredentials(credential))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode().name().equals("USER_PASSWORD_REQUIRED");
                })
                .verify();
    }

    @Test
    void validateCredentials_userNotFound() {
        AuthCredential credential = AuthCredentialMock.getDefault();

        when(userRepository.findByEmail(credential.getEmail())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.validateCredentials(credential))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode().name().equals("CREDENTIAL_NOT_FOUND");
                })
                .verify();
    }

    @Test
    void validateCredentials_invalidPassword() {
        User user = UserMock.getDefault();
        AuthCredential credential = AuthCredentialMock.getDefault();

        when(userRepository.findByEmail(credential.getEmail())).thenReturn(Mono.just(user));
        when(passwordHasher.verify(credential.getPassword(), user.getPassword())).thenReturn(false);

        StepVerifier.create(useCase.validateCredentials(credential))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode().name().equals("CREDENTIAL_NOT_FOUND");
                })
                .verify();
    }
}
