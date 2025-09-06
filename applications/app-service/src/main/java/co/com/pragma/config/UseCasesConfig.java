package co.com.pragma.config;

import co.com.pragma.model.authcredential.gateways.PasswordHasher;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.role.RoleUseCase;
import co.com.pragma.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "co.com.pragma.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = org.springframework.stereotype.Component.class)
        },
        useDefaultFilters = false
)
public class UseCasesConfig {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UseCasesConfig(RoleRepository roleRepository, UserRepository userRepository, PasswordHasher passwordHasher) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Bean
    public RoleUseCase roleUseCase() {
        return new RoleUseCase(roleRepository);
    }

    @Bean
    public UserUseCase userUseCase() {
        return new UserUseCase(userRepository, roleRepository, passwordHasher);
    }

}
