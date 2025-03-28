package com.example.testechatgemini.service;

import com.example.testechatgemini.model.Ocorrencia;
import com.example.testechatgemini.repository.OcorrenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    // Vamos adicionar os métodos depois
}
