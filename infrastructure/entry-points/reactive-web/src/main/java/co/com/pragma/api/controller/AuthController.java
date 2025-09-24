package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.AuthCredentialRequestDto;
import co.com.pragma.api.dto.response.AuthTokenResponseDto;
import co.com.pragma.api.handler.AuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones relacionadas con el login y autenticación de usuarios")
public class AuthController {

    private final AuthHandler authHandler;

    @PostMapping("/login")
    @Operation(
            summary = "Login de usuario",
            description = "Permite a un usuario autenticarse en la plataforma y recibir un token JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticación exitosa, token generado",
                            content = @Content(schema = @Schema(implementation = AuthTokenResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales incorrectas",
                            content = @Content()
                    )
            }
    )
    public Mono<AuthTokenResponseDto> login(@RequestBody AuthCredentialRequestDto requestDto) {
        return authHandler.login(requestDto);
    }
}
