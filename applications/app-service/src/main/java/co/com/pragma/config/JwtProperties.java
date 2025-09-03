package co.com.pragma.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secretKey;
    private long expiration; // en segundos

}
