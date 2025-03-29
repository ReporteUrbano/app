package com.example.reporteurbano.repository;

import com.example.reporteurbano.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    // métodos customizados vão aqui se precisar depois
}
