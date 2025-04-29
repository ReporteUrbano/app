package com.example.reporteurbano.controller;

import com.example.reporteurbano.config.JwtUtil;
import com.example.reporteurbano.model.LoginRequest;
import com.example.reporteurbano.model.Usuario;
import com.example.reporteurbano.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorCpf(loginRequest.getCpf());

            //se os dados estiverem incorretos ou o nome estiver incorreto
            if (usuario.isEmpty() || !usuario.get().getNome().equals(loginRequest.getNome())) {
                Map<String, String> errorBody = Map.of("error", "Credenciais inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
            }

            // Gera o token JWT
            String token = jwtUtil.generateToken(usuario.get().getCpf(), usuario.get().getId());

            //gera uma body response
            Map<String, Object> successBody = new HashMap<>();
            successBody.put("message", "Login realizado com sucesso!");
            successBody.put("userId", usuario.get().getId());
            successBody.put("token", token); // <--- Enviamos o token no body da resposta!

            return ResponseEntity.ok(successBody);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro interno ao tentar realizar login"));
        }
    }

}
