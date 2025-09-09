package co.com.pragma.api.config;

import co.com.pragma.api.dto.response.ErrorResponse;
import co.com.pragma.messagetranslator.MessageTranslator;
import co.com.pragma.usecase.common.messages.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageTranslator translator;

    public GlobalExceptionHandler(MessageTranslator translator) {
        this.translator = translator;
    }

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

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Object> handleValidationException(WebExchangeBindException ex) {
        // Tomamos todos los errores de campos
        var errors = ex.getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        var body = Map.of(
                "message", "Validation failed",
                "errors", errors,
                "status", HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        HttpStatus status =
                HttpStatus.valueOf(ex.getCode().getHttpStatus());

        // Traduce el mensaje con parámetros
        String message = translator.translate(ex.getCode(), ex.getParams());

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(
                        message,
                        status.value(),
                        status.getReasonPhrase(),
                        LocalDateTime.now()
                ));
    }




}
