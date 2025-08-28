package co.com.pragma.config;

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

    public UseCasesConfig(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public RoleUseCase roleUseCase() {
        return new RoleUseCase(roleRepository);
    }

    @Bean
    public UserUseCase userUseCase() {
        return new UserUseCase(userRepository);
    }

}
