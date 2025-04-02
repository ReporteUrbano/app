package com.example.reporteurbano.repository;

import com.example.reporteurbano.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCpf(String cpf);
    // Métodos customizados podem ser adicionados aqui
}
