package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleRepository roleRepository;

    public Mono<Role> createRole(Role role) {
        if (role.getName() == null || role.getName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.ROLE_NAME_REQUIRED, new Object[]{}));
        }
        return roleRepository.findByName(role.getName())
                .flatMap(existing -> Mono.<Role>error(new BusinessException(MessageCode.ROLE_NAME_REQUIRED, new Object[]{})))
                .switchIfEmpty(Mono.defer(() ->
                    roleRepository.save(role)
                ));
    }

    public Mono<Role> findByIdRole(UUID id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.ROLE_NOT_FOUND_BY_ID, new Object[]{id}))); //  mensaje si no existe
    }

    public Flux<Role> listAllRoles() {
        return roleRepository.findAll();
    }

    public Mono<Role> updateRole(UUID id, Role role) {
        if (role.getName() == null || role.getName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.ROLE_NAME_REQUIRED, new Object[]{}));
        }
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.ROLE_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing ->
                        roleRepository.findByName(role.getName())
                                .flatMap(duplicate -> {
                                    if (!duplicate.getId().equals(id)) {
                                        return Mono.error(new BusinessException(MessageCode.ROLE_ALREADY_EXISTS, new Object[]{id}));
                                    }
                                    return Mono.just(duplicate);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    existing.setName(role.getName());
                                    existing.setDescription(role.getDescription());
                                    return roleRepository.save(existing);
                                }))
                );
    }

    public Mono<Void> deleteRole(UUID id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.ROLE_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing -> roleRepository.deleteById(id));
    }
}
