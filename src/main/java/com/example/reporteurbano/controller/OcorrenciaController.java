package com.example.reporteurbano.controller;

import com.example.reporteurbano.dto.OcorrenciaDTO;
import com.example.reporteurbano.model.Ocorrencia;
import com.example.reporteurbano.service.OcorrenciaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.reporteurbano.service.GeminiService;

import java.util.Map;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    @Autowired
    private GeminiService geminiService;

    private final OcorrenciaService ocorrenciaService;

    @Autowired
    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    // Criar ou atualizar ocorrência
    @PostMapping
    public ResponseEntity<?> createOcorrencia(@RequestBody OcorrenciaDTO dto, HttpServletRequest request) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setTituloProblema(dto.tituloProblema());
        ocorrencia.setDescricao(dto.descricao());
        ocorrencia.setLocalizacao(dto.localizacao());
        ocorrencia.setFoto(dto.foto());
        ocorrencia.setIdUsuario(dto.userId());
        ocorrencia.setCategoria(dto.categoria());

        try {
            // Chama a IA com a descrição e a imagem Base64
            String respostaIA = geminiService.gerarOrientacaoIA(ocorrencia);

            // Salva a ocorrência normalmente
            Ocorrencia novaOcorrencia = ocorrenciaService.saveOcorrencia(ocorrencia);

            // Retorna tanto a ocorrência quanto a resposta da IA
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "ocorrencia", novaOcorrencia,
                            "descricaoIa", respostaIA // Retorna a resposta da IA
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao criar ocorrência", HttpStatus.BAD_REQUEST);
        }
    }

    // Buscar todas as ocorrências
    @EntityGraph(attributePaths = {"usuario"})
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

    // Buscar ocorrência por ID
    @EntityGraph(attributePaths = {"usuario"})
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

    // Retorna todas as ocorrências do usuário logado
    @EntityGraph(attributePaths = {"usuario"})
    @GetMapping("/all/{userId}")
    public ResponseEntity<?> getAllOcorrenciasByLogin(@PathVariable int userId) {
        try {
            List<Ocorrencia> ocorrencias = ocorrenciaService.getAllOcorrenciaByLogin(userId);
            return new ResponseEntity<>(ocorrencias, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao buscar ocorrências por login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Deletar ocorrência
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOcorrencia(@PathVariable int id) {
        try {
            ocorrenciaService.deleteOcorrencia(id);
            return new ResponseEntity<>("Ocorrência deletada com sucesso", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao deletar ocorrência", HttpStatus.BAD_REQUEST);
        }
    }
}
