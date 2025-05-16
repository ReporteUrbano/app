package com.example.reporteurbano.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.reporteurbano.model.Ocorrencia;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import org.json.JSONObject;

@Service
public class GeminiService {

    @Value("${google.ai.api-key}")
    private String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String gerarOrientacaoIA(Ocorrencia ocorrencia) {

        String endereco = obterEndereco(ocorrencia.getLocalizacao());
        System.out.println(endereco);
        try {
            // Prompt personalizado
            String prompt = "Com base na imagem, na descrição do problema urbano e na localização informada, preciso que seja bem preciso na localizacao, diga onde reportar esse problema, como fazer isso, e uma dica útil de como agir no curto prazo. "
                    + "Descrição: " + ocorrencia.getDescricao() + ". "
                    + "Localização: " + endereco
                    + "Preciso de um email e um telefone para enviar meu reporte e preciso da resposta muito curta, apenas o básico, somente as informaç~poes essenciais!!!";

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

    //localizacao em latitude e longitude
    public static String obterEndereco(String localizacao) {
        try {
            if (localizacao == null) {
                return "Formato inválido";
            }

            // Extrai os números de dentro de LatLng(...)
            String match = localizacao.replaceAll("[^\\d\\-.,]", ""); // Remove letras e parenteses
            String[] partes = match.split(",");
            if (partes.length < 2) {
                return localizacao;
            }

            double lat = Double.parseDouble(partes[0]);
            double lon = Double.parseDouble(partes[1]);

            String urlStr = String.format(Locale.US,
                    "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json",
                    lat, lon
            );
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            Scanner sc = new Scanner(conn.getInputStream());
            StringBuilder jsonStr = new StringBuilder();
            while (sc.hasNext()) {
                jsonStr.append(sc.nextLine());
            }
            sc.close();

            JSONObject json = new JSONObject(jsonStr.toString());
            JSONObject address = json.getJSONObject("address");

            String cidade = address.has("city") ? address.getString("city") :
                    address.has("town") ? address.getString("town") :
                            address.has("village") ? address.getString("village") : "Cidade desconhecida";

            String estado = address.optString("state", "Estado desconhecido");
            String pais = address.optString("country", "País desconhecido");

            return cidade + ", " + estado + ", " + pais;

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao obter endereço";
        }
    }
}
