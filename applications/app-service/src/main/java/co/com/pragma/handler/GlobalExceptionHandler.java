package co.com.pragma.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Mono;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Mono<ResponseEntity<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(java.util.UUID.class)) {
            String mensaje = "El ID debe ser un UUID válido. Valor recibido: " + ex.getValue();
            return Mono.just(ResponseEntity.badRequest().body(mensaje));
        }
        String mensaje = "Parámetro inválido: " + ex.getName() + ". Valor recibido: " + ex.getValue();
        return Mono.just(ResponseEntity.badRequest().body(mensaje));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleIllegalArgument(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage())));
    }



    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleAll(Exception ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ocurrió un error inesperado: " + ex.getMessage())
        );
    }


}
