package com.example.testechatgemini.controller;
import com.example.testechatgemini.model.Ocorrencia;
import com.example.testechatgemini.service.OcorrenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final OcorrenciaService ocorrenciaService;

    @Autowired
    public UsuarioController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    // Vamos adicionar os endpoints depois
}
