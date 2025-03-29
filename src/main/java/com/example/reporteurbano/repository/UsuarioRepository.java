package com.example.reporteurbano.repository;

import com.example.reporteurbano.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    // Métodos customizados podem ser adicionados aqui
}
