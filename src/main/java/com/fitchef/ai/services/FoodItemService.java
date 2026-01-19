package com.fitchef.ai.services;

import com.fitchef.ai.models.FoodItem;
import com.fitchef.ai.repositories.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private FoodItemRepository repository;

    public FoodItemService(FoodItemRepository repository) {
        this.repository = repository;
    }

    public FoodItem salvar(FoodItem item) {
        return repository.save(item);
    }

    public List<FoodItem> listar() {
        return repository.findAll();
    }

    public Optional<FoodItem> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public FoodItem alterar(Long id, FoodItem foodItem) {
        if (repository.findById(id).isPresent()) {
            foodItem.setId(id);
            return repository.save(foodItem);
        }
        return null;
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
