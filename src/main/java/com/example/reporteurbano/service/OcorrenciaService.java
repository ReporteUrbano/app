package com.example.reporteurbano.service;

import com.example.reporteurbano.config.JwtUtil;
import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.repository.OcorrenciaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OcorrenciaService {

    @Autowired
    private JwtUtil jwtUtil;

    private final OcorrenciaRepository OcorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository OcorrenciaRepository) {
        this.OcorrenciaRepository = OcorrenciaRepository;
    }

    // Criar ou atualizar usuário
    public Ocorrencia saveOcorrencia(Ocorrencia ocorrencia, HttpServletRequest request) {
        // Obtém o token do cookie
        String token = jwtUtil.getTokenFromCookies(request);

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não encontrado no cookie");
        }

        // Obtém o ID do usuário logado a partir do token JWT
        int idUsuario = jwtUtil.getUserIdFromToken(token);

        // Define o ID do usuário na ocorrência antes de salvar
        ocorrencia.setIdUsuario(idUsuario);

        return OcorrenciaRepository.save(ocorrencia);
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

    public List<Ocorrencia> getAllOcorrenciaByLogin(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromCookies(request);

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não encontrado no cookie");
        }

        // Obtém o ID do usuário logado a partir do token JWT
        int idUsuario = jwtUtil.getUserIdFromToken(token);

        return OcorrenciaRepository.findByIdUsuario(idUsuario);
    }
}
