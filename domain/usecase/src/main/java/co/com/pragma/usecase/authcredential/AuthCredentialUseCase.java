package co.com.pragma.usecase.authcredential;

import co.com.pragma.model.authcredential.AuthCredential;
import co.com.pragma.model.authcredential.gateways.PasswordHasher;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthCredentialUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final RoleRepository roleRepository; // para cargar el Role completo

    public Mono<User> validateCredentials(AuthCredential credential) {
        if (credential.getEmail() == null || credential.getEmail().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_EMAIL_REQUIRED, new Object[]{}));
        }
        if (credential.getPassword() == null || credential.getPassword().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.USER_PASSWORD_REQUIRED, new Object[]{}));
        }

        return userRepository.findByEmail(credential.getEmail())
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.CREDENTIAL_NOT_FOUNDL, new Object[]{credential.getEmail()})))
                .flatMap(user -> {
                    if (!passwordHasher.verify(credential.getPassword(), user.getPassword())) {
                        return Mono.error(new BusinessException(MessageCode.CREDENTIAL_NOT_FOUNDL, new Object[]{}));
                    }

                    // Cargar Role completo usando el UUID
                    return roleRepository.findById(user.getRole().getId())
                            .map(role -> {
                                user.setRole(role);
                                return user;
                            });
                });
    }
}


