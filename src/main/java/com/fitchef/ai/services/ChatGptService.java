package com.fitchef.ai.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ChatGptService {

    private final WebClient webClient;
    private final String apiKey;

    public ChatGptService(WebClient openAiWebClient) {
        this.webClient = openAiWebClient;
        this.apiKey = readApiKey();
    }

    private String readApiKey() {
        // Preferido
        String key = System.getenv("API.KEY");

        // Fallbacks (se você quiser manter compatibilidade com seu nome antigo)
        if (key == null || key.isBlank()) key = System.getenv("API_KEY");
        if (key == null || key.isBlank()) key = System.getenv("APIKEY");

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "OPENAI_API_KEY não encontrada nas variáveis de ambiente."
            );
        }
        return key;
    }

    public Mono<String> generateRecipe() {
        String prompt = "Me sugira uma receita simples com ingredientes comuns, porém sou diabético. " +
                "Inclua quantidades aproximadas e modo de preparo. Evite açúcar e farinha branca.";

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        return webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(
                                        new RuntimeException("OpenAI API erro HTTP " + resp.statusCode() + ": " + body)
                                ))
                )
                .bodyToMono(Map.class)
                .map(this::extractContentFromChatCompletions)
                .onErrorResume(ex -> Mono.just("Não foi possível gerar a receita agora. Motivo: " + ex.getMessage()));
    }

    @SuppressWarnings("unchecked")
    private String extractContentFromChatCompletions(Map<String, Object> response) {
        Object choicesObj = response.get("choices");
        if (!(choicesObj instanceof List<?> choices) || choices.isEmpty()) {
            return "Nenhuma receita foi gerada.";
        }

        Object firstChoiceObj = choices.get(0);
        if (!(firstChoiceObj instanceof Map<?, ?> firstChoice)) {
            return "Nenhuma receita foi gerada.";
        }

        Object messageObj = firstChoice.get("message");
        if (!(messageObj instanceof Map<?, ?> message)) {
            return "Nenhuma receita foi gerada.";
        }

        Object contentObj = message.get("content");
        return contentObj != null ? String.valueOf(contentObj) : "Nenhuma receita foi gerada.";
    }
}
