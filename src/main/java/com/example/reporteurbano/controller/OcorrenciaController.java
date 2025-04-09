package com.example.reporteurbano.controller;

import com.example.reporteurbano.config.JwtUtil;
import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.service.OcorrenciaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @Autowired
    private JwtUtil jwtUtil; // Classe utilitária para extrair informações do token
    @Autowired
    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    // Criar ou atualizar ocorrencia
    @PostMapping
    public ResponseEntity<?> createOcorrencia(@RequestBody Ocorrencia ocorrencia, HttpServletRequest request) {
        try {
            Ocorrencia novaOcorrencia = ocorrenciaService.saveOcorrencia(ocorrencia, request);
            return new ResponseEntity<>(novaOcorrencia, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao criar ocorrência", HttpStatus.BAD_REQUEST);
        }
    }


    // Buscar todos as ocorrencias
    @GetMapping
    public ResponseEntity<?> getAllOcorrencias() {
        try {
            List<Ocorrencia> ocorrencias = ocorrenciaService.getAllOcorrencias();
            return new ResponseEntity<>(ocorrencias, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao buscar ocorrências", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Buscar ocorrencia por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOcorrenciaById(@PathVariable int id) {
        try {
            Optional<Ocorrencia> ocorrencia = ocorrenciaService.getOcorrenciaById(id);
            if (ocorrencia.isPresent()) {
                return new ResponseEntity<>(ocorrencia.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Ocorrência não encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao buscar ocorrência por ID", HttpStatus.BAD_REQUEST);
        }
    }

    //retorna todasa as ocorrencias do usuairo logado
    @GetMapping("/all")
    public ResponseEntity<?> getAllOcorrenciasByLogin(HttpServletRequest request) {
        try {
            List<Ocorrencia> ocorrencias = ocorrenciaService.getAllOcorrenciaByLogin(request);
            return new ResponseEntity<>(ocorrencias, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao buscar ocorrências por login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Deletar ocorrencia
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteOcorrencia(@PathVariable int id) {
        try {
            ocorrenciaService.deleteOcorrencia(id);
            return new ResponseEntity<>("Ocorrência deletada com sucesso", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao deletar ocorrência", HttpStatus.BAD_REQUEST);
        }
    }
    }