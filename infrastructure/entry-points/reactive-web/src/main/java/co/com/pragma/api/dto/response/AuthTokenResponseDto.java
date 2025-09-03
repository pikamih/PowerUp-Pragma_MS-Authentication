package co.com.pragma.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokenResponseDto {

    private String name;
    private String email;
    private String role;
    private String token;
    private String expiresAt;
}
