package com.example.reporteurbano.controller;

import com.example.reporteurbano.dto.LoginRequestDTO;
import com.example.reporteurbano.dto.RegisterRequestDTO;
import com.example.reporteurbano.dto.ResponseDTO;
import com.example.reporteurbano.infra.security.TokenService;
import com.example.reporteurbano.model.Usuario;
import com.example.reporteurbano.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository repository;
    private final TokenService tokenService;

    public AuthController(UsuarioRepository repository, TokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
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
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body){
        Optional<Usuario> user =  this.repository.findByCpf(body.cpf());
        if (user.isEmpty()){
            Usuario newUser = new Usuario();
            newUser.setNome(body.nome());
            newUser.setCpf(body.cpf());
            newUser.setCep(body.cep());
            newUser.setGenero(body.genero());
            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getId(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
