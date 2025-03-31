package com.example.reporteurbano.controller;

import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.service.OcorrenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @Autowired
    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    // Criar ou atualizar usuário
    @PostMapping
    public Ocorrencia createOrUpdateUsuario(@RequestBody Ocorrencia ocorrencia) {
        return ocorrenciaService.saveOcorrencia(ocorrencia);
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

    // Deletar usuário
    @DeleteMapping("/{id}")
    public void deleteOcorrencia(@PathVariable int id) {
        ocorrenciaService.deleteOcorrencia(id);
    }
}
