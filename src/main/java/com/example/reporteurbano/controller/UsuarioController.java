package com.example.reporteurbano.controller;

import com.example.reporteurbano.model.Usuario;
import com.example.reporteurbano.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Criar ou atualizar usuário
    @PostMapping("/register")
    public ResponseEntity<?> createOrUpdateUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            if(novoUsuario == null){
                return new ResponseEntity<>("CPF já cadastrado", HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao cadastrar/atualizar usuário", HttpStatus.BAD_REQUEST);
        }
    }
    // Buscar todos os usuários
    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao buscar usuários", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable int id) {
        try {
            Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao buscar usuário por ID", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> buscarPorCpf(@PathVariable String cpf) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorCpf(cpf);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao buscar usuário por CPF", HttpStatus.BAD_REQUEST);
        }
    }


    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int id) {
        try {
            usuarioService.deleteUsuario(id);
            return new ResponseEntity<>("Usuário deletado com sucesso", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao deletar usuário", HttpStatus.BAD_REQUEST);
        }
    }
}