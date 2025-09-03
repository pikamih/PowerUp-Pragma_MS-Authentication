package co.com.pragma.model.authtoken;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthToken {

    private String name;
    private String email;
    private String documentId;
    private String role;
    private String token;
    private String expiresAt;

}
