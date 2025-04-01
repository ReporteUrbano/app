package com.example.reporteurbano.repository;

import com.example.reporteurbano.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    Optional<UsuarioModel> findByCpf(String cpf);
    // Métodos customizados podem ser adicionados aqui
}
