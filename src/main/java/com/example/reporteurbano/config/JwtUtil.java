package com.example.reporteurbano.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    //gera uma chave que sera responsavel pro gerar o jwt
    //gera uma chave que sera responsavel pro gerar o jwt
    private final SecretKey secretKey;

    // Obtém a chave secreta do application.properties
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // Garante que a chave seja de 256 bits
    }

    public String generateToken(String cpf) {
        return Jwts.builder()
                .setSubject(cpf)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(secretKey)
                .compact();
    }

    public String extractCpf(String token) {
        return getClaims(token).getSubject();
    } //pega o cpf do token

    public boolean validateToken(String token, String cpf) {
        return extractCpf(token).equals(cpf) && !isTokenExpired(token);
    } //valida token

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


