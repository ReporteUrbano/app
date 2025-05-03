package com.example.reporteurbano.repository;

import com.example.reporteurbano.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    List<Ocorrencia> findByIdUsuario(int id);
}
