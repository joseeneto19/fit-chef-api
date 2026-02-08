package com.fitchef.ai.controllers;

import com.fitchef.ai.services.ChatGptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RecipeController {

    private ChatGptService service;

    public RecipeController(ChatGptService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> generateRecipe() {
        return service.generateRecipe();
    }
}
