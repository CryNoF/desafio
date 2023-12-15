package com.cnavarrete.desafio.constants;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class SecurityConstants {

    // Spring Security
    public static final String LOGIN_URL = "/login";

    public static final String H2_CONSOLE = "/h2-console/**";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    public static final String SUPER_SECRET_KEY = "c3VwZXJrZXkgcGFyYSBkZXNhZmlvIGRlIGNydWQgY29uIGp3dCBjbmF2YXJyZXRlLiBBaG9yYSBhZ3JlZ28gbWFzIGluZm9ybWFjaW9uIHBhcmEgZXh0ZW5kZXIgZWwgdG9rZW4=";
    public static final long TOKEN_EXPIRATION_TIME = 86_400; // 1 dia

    public static Key getSigningKeyB64(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
