package com.example.reporteurbano.service;

import com.example.reporteurbano.dto.OcorrenciaDTO;
import com.example.reporteurbano.infra.security.TokenService;
import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.repository.OcorrenciaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OcorrenciaService {

    @Autowired
    private TokenService tokenService;

    private final OcorrenciaRepository OcorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository OcorrenciaRepository) {
        this.OcorrenciaRepository = OcorrenciaRepository;
    }

    // Criar ou atualizar usuário
    public Ocorrencia saveOcorrencia(Ocorrencia ocorrencia) {
        return OcorrenciaRepository.save(ocorrencia);
    }

    // Buscar todas as ocorrências
    public List<Ocorrencia> getAllOcorrencias() {
        return OcorrenciaRepository.findAll();
    }

    // Buscar uma ocorrência por ID
    public Optional<Ocorrencia> getOcorrenciaById(long id) {
        return OcorrenciaRepository.findById(id);
    }

    // Deletar uma ocorrência
    public void deleteOcorrencia(long id) {
        OcorrenciaRepository.deleteById(id);
    }

    public List<Ocorrencia> getAllOcorrenciaByLogin(int userId) {
        return OcorrenciaRepository.findByIdUsuario(userId);
    }
}
