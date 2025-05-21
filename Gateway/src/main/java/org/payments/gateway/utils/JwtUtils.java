package org.payments.gateway.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = System.getenv("JWT_SECRET");
    private static final long EXPIRATION_TIME = Long.parseLong(System.getenv("JWT_EXPIRY_TIME"));


    public static String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY.getBytes()));
    }


    public static String getUser(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }
}
