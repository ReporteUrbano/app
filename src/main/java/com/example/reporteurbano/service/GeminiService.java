package com.example.reporteurbano.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.reporteurbano.model.Ocorrencia;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

@Service
public class GeminiService {

    @Value("${google.ai.api-key}")
    private String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String gerarOrientacaoIA(Ocorrencia ocorrencia) {
        try {
            // Prompt personalizado
            String prompt = "Com base na imagem, na descrição do problema urbano e na localização informada, diga onde reportar esse problema, como fazer isso, e uma dica útil de como agir no curto prazo. "
                    + "Descrição: " + ocorrencia.getDescricao() + ". "
                    + "Localização: " + ocorrencia.getLocalizacao()
                    + "Preciso de um email e um telefone para enviar meu reporte e preciso da resposta mais curta e objetiva possível!!!";

            // Remover o prefixo "data:image/jpeg;base64," ou "data:image/png;base64,"
            String fotoBase64 = ocorrencia.getFoto();
            if (fotoBase64 != null && fotoBase64.contains(",")) {
                fotoBase64 = fotoBase64.split(",")[1];  // Remove o prefixo
            }

            // Montando o JSON da requisição
            String jsonRequest = "{"
                    + "\"contents\": [{"
                    + "\"parts\": ["
                    + "{\"text\": \"" + prompt + "\"},"
                    + "{"
                    + "\"inline_data\": {"
                    + "\"mime_type\": \"image/jpeg\","
                    + "\"data\": \"" + fotoBase64 + "\""
                    + "}"
                    + "}"
                    + "]"
                    + "}]"
                    + "}";

            // Criando a requisição HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            // Enviando a requisição e recebendo a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Se a resposta for bem-sucedida, processa a resposta da IA
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());
                return jsonNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            } else {
                return "Erro ao buscar resposta da IA (HTTP " + response.statusCode() + ")";
            }
        } catch (IOException | InterruptedException e) {
            return "Erro ao processar requisição da IA: " + e.getMessage();
        }
    }
}
