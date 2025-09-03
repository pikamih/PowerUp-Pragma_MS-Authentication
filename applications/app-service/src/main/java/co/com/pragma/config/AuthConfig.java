package co.com.pragma.config;

import co.com.pragma.model.authcredential.gateways.PasswordHasher;
import co.com.pragma.model.authtoken.gateways.AuthTokenRepository;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.authcredential.AuthCredentialUseCase;
import co.com.pragma.usecase.authtoken.AuthTokenUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public AuthCredentialUseCase authCredentialUseCase(UserRepository userRepository, PasswordHasher passwordHasher, RoleRepository roleRepository) {
        return new AuthCredentialUseCase(userRepository, passwordHasher,roleRepository);
    }

    @Bean
    public AuthTokenUseCase authTokenUseCase(AuthTokenRepository authTokenRepository) {
        return new AuthTokenUseCase(authTokenRepository);
    }
}

