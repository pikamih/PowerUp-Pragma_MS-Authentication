package co.com.pragma.model.authcredential.gateways;

public interface PasswordHasher {
    String hash(String plainPassword);
    boolean verify(String plainPassword, String hashedPassword);
}
