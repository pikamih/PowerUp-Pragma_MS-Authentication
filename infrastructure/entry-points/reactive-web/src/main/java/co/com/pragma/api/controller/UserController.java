package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.UserRequestDto;
import co.com.pragma.api.dto.response.UserResponseDto;
import co.com.pragma.api.mapper.UserWebMapper;
import co.com.pragma.usecase.user.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;
    private final UserWebMapper userWebMapper;

    @PreAuthorize("hasAuthority('ADMIN','ASESOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        return userUseCase.createUser(userWebMapper.toDomain(dto))
                .map(userWebMapper::toResponse);
    }



    @GetMapping
    public Flux<UserResponseDto> getAllUsers() {
        return userUseCase.listUsers()
                .map(userWebMapper::toResponse);
    }


    @GetMapping("/{id}")
    public Mono<UserResponseDto> getUserById(@PathVariable("id") String id) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format"));
        }

        return userUseCase.getUserById(userId)
                .map(userWebMapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<UserResponseDto>  updateUser(@PathVariable("id") String id,
                                                            @RequestBody UserRequestDto dto) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format"));
        }

        return userUseCase.updateUser(userId, userWebMapper.toDomain(dto))
                .map(userWebMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable("id") String id) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.badRequest().body("Formato de UUID inválido"));
        }

        return userUseCase.deleteUser(userId)
                .then(Mono.just(ResponseEntity.ok("Usuario eliminado correctamente")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body(e.getMessage())));
    }

}
