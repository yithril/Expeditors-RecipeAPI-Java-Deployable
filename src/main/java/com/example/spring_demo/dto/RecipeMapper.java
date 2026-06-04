package com.example.spring_demo.dto;

import com.example.spring_demo.models.Recipe;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeMapper {

    public RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt()
        );
    }
    public List<RecipeResponse> toResponseList(List<Recipe> recipes) {
        return recipes.stream().map(this::toResponse).toList();
    }

    public Recipe toEntity(RecipeCreateRequest request) {
        return new Recipe(request.getName(), request.getIngredients(), request.getInstructions());
    }

    public void applyUpdate(Recipe recipe, RecipeUpdateRequest request) {
        recipe.setName(request.getName());
        recipe.setIngredients(request.getIngredients());
        recipe.setInstructions(request.getInstructions());
    }

    public void applyPatch(Recipe recipe, RecipePatchRequest patch) {
        if (patch.getName() != null) {
            recipe.setName(patch.getName());
        }
        if (patch.getIngredients() != null) {
            recipe.setIngredients(patch.getIngredients());
        }
        if (patch.getInstructions() != null) {
            recipe.setInstructions(patch.getInstructions());
        }
    }
}
