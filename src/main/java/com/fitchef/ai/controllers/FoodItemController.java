package com.fitchef.ai.controllers;

import com.fitchef.ai.models.FoodItem;
import com.fitchef.ai.services.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/food")
public class FoodItemController {

    private FoodItemService service;

    public FoodItemController(FoodItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FoodItem> criar(@RequestBody FoodItem foodItem) {
        return ResponseEntity.ok(service.salvar(foodItem));
    }

    @GetMapping
    public ResponseEntity<List<FoodItem>> listar() {
        List<FoodItem> listar = service.listar();
        return ResponseEntity.ok(listar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<FoodItem>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> alterar(@PathVariable Long id, @RequestBody FoodItem foodItem) {
        Optional<FoodItem> existById = service.buscarPorId(id);
        if (existById.isPresent()) {
            return ResponseEntity.ok(service.alterar(id, foodItem));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
