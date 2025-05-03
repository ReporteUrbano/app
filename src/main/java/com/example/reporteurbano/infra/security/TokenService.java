package com.example.reporteurbano.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.reporteurbano.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.ZoneOffset;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    // Gera o token JWT com CPF como subject e ID como claim
    public String generateToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("ReporteUrbano")
                    .withSubject(user.getCpf()) // CPF como subject
                    .withClaim("userId", user.getId()) // ID como claim
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token", exception);
        }
    }

    // Validando Token
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("ReporteUrbano")
                    .build()
                    .verify(token)
                    .getSubject();

        }catch (JWTVerificationException exception){
            return null;
        }
    }

    //Extrai o CPF pelo subject do token
    public String getSubject(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("ReporteUrbano")
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject(); // ← CPF
    }

    //Extrai o ID pelo claim do token
    public int getUserId(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("ReporteUrbano")
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("userId").asInt(); // ← ID
    }

    // Gerando data de expiração do token ( 2 horas )
    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}

