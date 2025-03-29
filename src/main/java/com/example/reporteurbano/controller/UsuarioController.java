package com.example.reporteurbano.controller;

import com.example.reporteurbano.model.UsuarioModel;
import com.example.reporteurbano.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Criar ou atualizar usuário
    @PostMapping
    public UsuarioModel createOrUpdateUsuario(@RequestBody UsuarioModel usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    // Buscar todos os usuários
    @GetMapping
    public List<UsuarioModel> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public Optional<UsuarioModel> getUsuarioById(@PathVariable int id) {
        return usuarioService.getUsuarioById(id);
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable int id) {
        usuarioService.deleteUsuario(id);
    }
}
