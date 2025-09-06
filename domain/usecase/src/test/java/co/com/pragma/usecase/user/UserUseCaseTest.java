package co.com.pragma.usecase.user;

import co.com.pragma.model.authcredential.gateways.PasswordHasher;
import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import co.com.pragma.usecase.mock.RoleMock;
import co.com.pragma.usecase.mock.UserMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordHasher passwordHasher; // <- agregado

    @InjectMocks
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock de PasswordHasher para que siempre devuelva algo
        when(passwordHasher.hash(anyString())).thenReturn("hashedPassword");
    }

    // ---------- CREATE ----------
    @Test
    void createUser_success() {
        User user = UserMock.getDefault();
        Role role = RoleMock.getDefault();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existsByDocumentId(user.getDocumentId())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(user.getRole().getName())).thenReturn(Mono.just(role));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.createUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void createUser_emailEmpty_shouldThrow() {
        User user = UserMock.getWithCustomEmail("");

        StepVerifier.create(useCase.createUser(user))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode() == MessageCode.USER_EMAIL_REQUIRED;
                })
                .verify();
    }

    @Test
    void createUser_emailAlreadyExists_shouldThrow() {
        User user = UserMock.getDefault();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
        when(roleRepository.findByName(any())).thenReturn(Mono.just(RoleMock.getDefault()));

        StepVerifier.create(useCase.createUser(user))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode() == MessageCode.USER_EMAIL_ALREADY_EXISTS;
                })
                .verify();
    }

    // ---------- FIND BY ID ----------
    @Test
    void findByIdUser_success() {
        User user = UserMock.getDefault();
        when(userRepository.findById(user.getId())).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.getUserById(user.getId()))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void findByIdUser_notFound_shouldThrow() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getUserById(id))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode() == MessageCode.USER_NOT_FOUND_BY_ID;
                })
                .verify();
    }

    // ---------- LIST ALL ----------
    @Test
    void listAllUsers_success() {
        User user1 = UserMock.getDefault();
        User user2 = UserMock.getWithCustomEmail("jane@example.com");

        when(userRepository.findAll()).thenReturn(Flux.just(user1, user2));

        StepVerifier.create(useCase.listUsers())
                .expectNext(user1, user2)
                .verifyComplete();
    }

    @Test
    void listAllUsers_empty() {
        when(userRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.listUsers())
                .verifyComplete();
    }

    // ---------- UPDATE ----------
    @Test
    void updateUser_success() {
        User existing = UserMock.getDefault(); // email: default@example.com
        User updated = UserMock.getWithCustomEmail("updated@example.com"); // email distinto
        Role role = RoleMock.getDefault();

        // Mocks
        when(userRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(userRepository.findByEmail(updated.getEmail())).thenReturn(Mono.empty());
        when(userRepository.existsByEmail(updated.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existsByDocumentId(updated.getDocumentId())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(updated.getRole().getName())).thenReturn(Mono.just(role));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(updated));

        // Verificación
        StepVerifier.create(useCase.updateUser(existing.getId(), updated))
                .expectNext(updated)
                .verifyComplete();
    }



    @Test
    void updateUser_notFound_shouldThrow() {
        UUID id = UUID.randomUUID();
        User updated = UserMock.getDefault();
        when(userRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateUser(id, updated))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode() == MessageCode.USER_NOT_FOUND_BY_ID;
                })
                .verify();
    }

    @Test
    void updateUser_emailAlreadyExists_shouldThrow() {
        User existing = UserMock.getDefault();
        User duplicate = UserMock.getWithCustomEmail("duplicate@example.com");

        when(userRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(userRepository.findByEmail(duplicate.getEmail()))
                .thenReturn(Mono.just(UserMock.getWithCustomEmail("duplicate@example.com")));
        when(userRepository.existsByEmail(duplicate.getEmail())).thenReturn(Mono.just(true));
        when(userRepository.existsByDocumentId(duplicate.getDocumentId())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(duplicate.getRole().getName())).thenReturn(Mono.just(RoleMock.getDefault()));

        StepVerifier.create(useCase.updateUser(existing.getId(), duplicate))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode() == MessageCode.USER_EMAIL_ALREADY_EXISTS;
                })
                .verify();
    }

    // ---------- DELETE ----------
    @Test
    void deleteUser_success() {
        User user = UserMock.getDefault();
        when(userRepository.findById(user.getId())).thenReturn(Mono.just(user));
        when(userRepository.delete(user.getId())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteUser(user.getId()))
                .verifyComplete();
    }

    @Test
    void deleteUser_notFound_shouldThrow() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteUser(id))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getMessageCode() == MessageCode.USER_NOT_FOUND_BY_ID;
                })
                .verify();
    }
}
