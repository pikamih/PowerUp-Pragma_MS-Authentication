package co.com.pragma.jwt;

public record TokenWithExpiry(String token, long expiresAt) {}
