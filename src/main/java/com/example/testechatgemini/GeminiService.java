package com.example.testechatgemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.io.IOException;

@Service
public class GeminiService {

    @Value("${google.ai.api-key}")
    private String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String gerarRespostaComImagem(String pergunta, MultipartFile imagem) {
        try {
            // Converter a imagem para Base64
            String imagemBase64 = Base64.getEncoder().encodeToString(imagem.getBytes());

            // Criar o JSON da requisição
            String jsonRequest = "{"
                    + "\"contents\": [{"
                    + "\"parts\": ["
                    + "{\"text\": \"" + pergunta + "\"},"
                    + "{"
                    + "\"inline_data\": {"
                    + "\"mime_type\": \"image/jpeg\","
                    + "\"data\": \"" + imagemBase64 + "\""
                    + "}"
                    + "}"
                    + "]"
                    + "}]"
                    + "}";

            // Criar cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Criar requisição HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            // Enviar requisição e obter resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar status da resposta
            if (response.statusCode() == 200) {
                // Processar resposta JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());

                return jsonNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            } else {
                return "Erro ao buscar resposta na IA. Código HTTP: " + response.statusCode();
            }
        } catch (IOException | InterruptedException e) {
            return "Erro ao processar a requisição: " + e.getMessage();
        }
    }
}
