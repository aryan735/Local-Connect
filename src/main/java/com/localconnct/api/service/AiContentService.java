package com.localconnct.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localconnct.api.dto.ServiceContentDto;
import com.localconnct.api.exception.GeminiApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AiContentService {

private final WebClient webClient;

    public AiContentService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Value("${gemini.url}")
    private String geminiUrl;
    @Value("${gemini.api}")
    private String geminiApi;

    public String  generateServiceContent(ServiceContentDto serviceContent) {
        String prompt = buildPrompt(serviceContent);
        Map<String,Object> requestBody = Map.of(
                "contents",new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text",prompt)
                                }
                        )
                }
        );

        String response = webClient.post()
                .uri(geminiUrl+geminiApi)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNote = mapper.readTree(response);
            return rootNote.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText().replace("\\*","");

        } catch (JsonProcessingException e) {
            throw new GeminiApiException("Error occurred while extracting response.",e);
        }
    }

    private String buildPrompt(ServiceContentDto serviceContent) {
        return "generate a professional title based on this title :" + serviceContent.getTitle() + " and a professional description based on these keywords : " + serviceContent.getKeywords() + " Do not include any introductory phrases or explanations.";
    }
}
