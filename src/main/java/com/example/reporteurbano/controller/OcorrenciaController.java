package com.example.reporteurbano.controller;

import com.example.reporteurbano.config.JwtUtil;
import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.service.OcorrenciaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @Autowired
    private JwtUtil jwtUtil; // Classe utilitária para extrair informações do token
    @Autowired
    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    // Criar ou atualizar usuário
    @PostMapping
    public Ocorrencia createOcorrencia(@RequestBody Ocorrencia ocorrencia, HttpServletRequest request) {
        return ocorrenciaService.saveOcorrencia(ocorrencia, request);
    }

    // Buscar todos os usuários
    @GetMapping
    public List<Ocorrencia> getAllUsuarios() {
        return ocorrenciaService.getAllOcorrencias();
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public Optional<Ocorrencia> getOcorrenciaById(@PathVariable int id) {
        return ocorrenciaService.getOcorrenciaById(id);
    }

    @GetMapping("/all") //retorna todasa as ocorrencias do usuairo logado
    public List<Ocorrencia> getAllOcorrenciaByLogin(HttpServletRequest request){
        return ocorrenciaService.getAllOcorrenciaByLogin(request);
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public void deleteOcorrencia(@PathVariable int id) {
        ocorrenciaService.deleteOcorrencia(id);
    }
}
