package com.fitchef.ai.controllers;

import com.fitchef.ai.models.FoodItem;
import com.fitchef.ai.services.ChatGptService;
import com.fitchef.ai.services.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class RecipeController {

    private ChatGptService service;
    private FoodItemService foodItemService;

    public RecipeController(ChatGptService service, FoodItemService foodItemService) {
        this.service = service;
        this.foodItemService = foodItemService;
    }

    @GetMapping("/generate")
    public Mono<ResponseEntity<String>> generateRecipe() {
        List<FoodItem> foodItems = foodItemService.listar();
        return service.generateRecipe(foodItems)
                .map(recipe -> ResponseEntity.ok(recipe))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
