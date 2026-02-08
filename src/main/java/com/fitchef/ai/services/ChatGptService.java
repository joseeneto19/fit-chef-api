package com.fitchef.ai.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ChatGptService {

    private final WebClient webClient;
    private final String apiKey = System.getenv("API.KEY");

    public ChatGptService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> generateRecipe() {
        String prompt = "Me sugira uma receita simples com ingredientes comuns";
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "input", prompt
        );

        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return message.get("content").toString();
                    }
                    return "Nenhuma receita foi gerada.";
                });
    }
}
