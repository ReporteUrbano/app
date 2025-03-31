package com.example.reporteurbano.service;

import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.repository.OcorrenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository OcorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository OcorrenciaRepository) {
        this.OcorrenciaRepository = OcorrenciaRepository;
    }

    // Criar ou atualizar usuário
    public Ocorrencia saveOcorrencia(Ocorrencia Ocorrencia) {
        return OcorrenciaRepository.save(Ocorrencia);
    }

    // Buscar todos os usuários
    public List<Ocorrencia> getAllOcorrencias() {
        return OcorrenciaRepository.findAll();
    }

    // Buscar um usuário por ID
    public Optional<Ocorrencia> getOcorrenciaById(long id) {
        return OcorrenciaRepository.findById(id);
    }

    // Deletar um usuário
    public void deleteOcorrencia(long id) {
        OcorrenciaRepository.deleteById(id);
    }
}
