package co.com.pragma.model.role;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder        // <--- esto es lo que falta
public class Role {
    private UUID id;
    private String name;
    private String description;
}
