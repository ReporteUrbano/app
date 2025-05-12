package com.example.reporteurbano.controller;

import com.example.reporteurbano.dto.LoginRequestDTO;
import com.example.reporteurbano.dto.RegisterRequestDTO;
import com.example.reporteurbano.dto.ResponseDTO;
import com.example.reporteurbano.infra.security.TokenService;
import com.example.reporteurbano.model.Usuario;
import com.example.reporteurbano.repository.UsuarioRepository;
import com.example.reporteurbano.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository repository;
    private final TokenService tokenService;

    @Autowired
    public AuthController(UsuarioRepository repository, TokenService tokenService, UsuarioService usuarioService) {
        this.repository = repository;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body){
        Usuario user =  this.repository.findByCpf(body.cpf()).orElseThrow(() -> new RuntimeException("User not found"));
        if(Objects.equals(user.getCpf(), body.cpf())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getId(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body){
        Optional<Usuario> existingUser = repository.findByCpf(body.cpf());
        if (existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"CPF já cadastrado\"}");
        }

        Usuario newUser = new Usuario();
        newUser.setNome(body.nome());
        newUser.setCpf(body.cpf());
        newUser.setCep(body.cep());
        newUser.setGenero(body.genero());

        Usuario savedUser = usuarioService.cadastrarUsuario(newUser);

        if (savedUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Erro ao salvar o usuário\"}");
        }

        String token = this.tokenService.generateToken(savedUser);
        return ResponseEntity.ok(new ResponseDTO(savedUser.getId(), token));
    }
}

