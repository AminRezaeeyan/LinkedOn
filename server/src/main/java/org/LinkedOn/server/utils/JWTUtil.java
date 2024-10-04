package org.LinkedOn.server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.net.httpserver.HttpExchange;
import org.LinkedOn.server.exceptions.UnauthorizedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JWTUtil {
    private static final String SECRET;
    private static final String ISSUER;

    static {
        SECRET = Configuration.getValue("jwt", "secret");
        ISSUER = Configuration.getValue("jwt", "issuer");
    }

    public static String createToken(String userId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create().withIssuer(ISSUER).withSubject(userId).sign(algorithm);
    }

    public static String verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String verifyToken(HttpExchange exchange) throws UnauthorizedException {
        if (HTTP.isPublicEndPoint(exchange)) return null;

        String authorizationHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException();
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        String userId = verifyToken(token);

        if (userId == null) {
            throw new UnauthorizedException();
        }

        return userId;
    }
}
