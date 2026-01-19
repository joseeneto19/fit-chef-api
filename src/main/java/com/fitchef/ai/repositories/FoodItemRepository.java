package com.fitchef.ai.repositories;

import com.fitchef.ai.models.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}
