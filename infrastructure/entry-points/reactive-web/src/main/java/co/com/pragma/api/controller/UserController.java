package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.UserRequestDto;
import co.com.pragma.api.dto.response.UserResponseDto;
import co.com.pragma.api.mapper.UserWebMapper;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping
    public Mono<ResponseEntity<UserResponseDto>> createUser(@RequestBody UserRequestDto dto) {
        User userDomain = UserWebMapper.toDomain(dto);
        return userUseCase.createUser(userDomain)
                .map(savedUser -> ResponseEntity.ok(UserWebMapper.toResponse(savedUser)));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<UserResponseDto>>> getAllUsers() {
        Flux<UserResponseDto> usersFlux = userUseCase.getAllUsers()
                .map(UserWebMapper::toResponse);
        return Mono.just(ResponseEntity.ok(usersFlux));
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponseDto>> getUserById(@PathVariable("id") String id) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format"));
        }

        return userUseCase.getUserById(userId)
                .map(user -> ResponseEntity.ok(UserWebMapper.toResponse(user)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponseDto>> updateUser(@PathVariable("id") String id,
                                                            @RequestBody UserRequestDto dto) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format"));
        }

        User userDomain = UserWebMapper.toDomain(dto);
        return userUseCase.updateUser(userId, userDomain)
                .map(updatedUser -> ResponseEntity.ok(UserWebMapper.toResponse(updatedUser)));
    }

    @DeleteMapping("/{id}")
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
