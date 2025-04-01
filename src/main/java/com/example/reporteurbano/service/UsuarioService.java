package com.example.reporteurbano.service;

import com.example.reporteurbano.model.UsuarioModel;
import com.example.reporteurbano.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Criar ou atualizar usuário
    public UsuarioModel saveUsuario(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    // Buscar todos os usuários
    public List<UsuarioModel> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar um usuário por ID
    public Optional<UsuarioModel> getUsuarioById(int id) {
        return usuarioRepository.findById(id);
    }

    // Deletar um usuário
    public void deleteUsuario(int id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<UsuarioModel> getUsuarioByCPF(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }
}
