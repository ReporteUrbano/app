package com.example.reporteurbano.service;

import com.example.reporteurbano.repository.OcorrenciaRepository;
import org.springframework.stereotype.Service;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    // Vamos adicionar os métodos depois
}
