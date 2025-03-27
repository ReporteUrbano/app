package com.example.testechatgemini;

import com.example.testechatgemini.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ia")
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping(value = "/perguntar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String perguntarIA(
            @RequestParam("pergunta") String pergunta,
            @RequestParam("imagem") MultipartFile imagem) {

        return geminiService.gerarRespostaComImagem(pergunta, imagem);
    }
}
