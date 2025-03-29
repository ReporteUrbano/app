package com.example.testechatgemini.service;

import com.example.testechatgemini.model.Ocorrencia;
import com.example.testechatgemini.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Vamos adicionar os métodos depois
}