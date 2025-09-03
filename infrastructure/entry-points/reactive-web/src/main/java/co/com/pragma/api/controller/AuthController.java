package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.AuthCredentialRequestDto;
import co.com.pragma.api.dto.response.AuthTokenResponseDto;
import co.com.pragma.api.handler.AuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthHandler authHandler;

    @PostMapping("/login")
    public Mono<AuthTokenResponseDto> login(@RequestBody AuthCredentialRequestDto requestDto) {
        return authHandler.login(requestDto);
    }
}
