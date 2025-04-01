package com.example.reporteurbano.controller;

import com.example.reporteurbano.config.JwtUtil;
import com.example.reporteurbano.model.LoginRequest;
import com.example.reporteurbano.model.UsuarioModel;
import com.example.reporteurbano.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil; // Classe que irá gerar o token
    private final UsuarioService usuarioService; // Classe de serviço que lida com usuários

    public AuthController(JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<UsuarioModel> usuario = usuarioService.getUsuarioByCPF(loginRequest.getCpf());

        if (usuario.isEmpty() || !usuario.get().getNome().equals(loginRequest.getNome())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        // Gera o token JWT
        String token = jwtUtil.generateToken(loginRequest.getCpf());

        // Define um cookie com o token
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Em produção, deve ser true para HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hora

        response.addCookie(cookie);

        return ResponseEntity.ok("Login realizado com sucesso!");
    }
}
