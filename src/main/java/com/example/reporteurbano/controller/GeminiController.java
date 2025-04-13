//package com.example.reporteurbano.controller;
//
//import com.example.reporteurbano.model.Ocorrencia;
//import com.example.reporteurbano.service.GeminiService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/ia")
//public class GeminiController {
//
//    @Autowired
//    private GeminiService geminiService;
//
//    @PostMapping("/perguntar")
//    public String perguntarIA(
//            @RequestParam("descricao") String descricao,
//            @RequestParam("imagem") String imagemBase64,
//            @RequestParam("localizacao") String localizacao
//    ) {
//        // Monta uma ocorrência fictícia para testar a IA
//        Ocorrencia ocorrencia = new Ocorrencia();
//        ocorrencia.setDescricao(descricao);
//        ocorrencia.setFoto(imagemBase64);
//        ocorrencia.setLocalizacao(localizacao);
//
//        return geminiService.gerarOrientacaoIA(ocorrencia);
//    }
//}
