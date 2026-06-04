package com.example.spring_demo.services;

import com.example.spring_demo.dto.*;
import com.example.spring_demo.models.Recipe;
import com.example.spring_demo.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    public List<RecipeResponse> findAll(String name, String ingredients) {
        List<Recipe> recipes;
        if (name != null && ingredients != null) {
            recipes = recipeRepository.findByNameContainingAndIngredientsContaining(name, ingredients);
        } else if (name != null) {
            recipes = recipeRepository.findByNameContaining(name);
        } else if (ingredients != null) {
            recipes = recipeRepository.findByIngredientsContaining(ingredients);
        } else {
            recipes = recipeRepository.findAll();
        }
        return recipeMapper.toResponseList(recipes);
    }

    public Optional<RecipeResponse> findById(Long id) {
        return recipeRepository.findById(id).map(recipeMapper::toResponse);
    }

    public RecipeResponse create(RecipeCreateRequest request) {
        Recipe saved = recipeRepository.save(recipeMapper.toEntity(request));
        return recipeMapper.toResponse(saved);
    }

    public Optional<RecipeResponse> update(Long id, RecipeUpdateRequest request) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipeMapper.applyUpdate(recipe, request);
                    return recipeMapper.toResponse(recipeRepository.save(recipe));
                });
    }

    public Optional<RecipeResponse> patch(Long id, RecipePatchRequest patch) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipeMapper.applyPatch(recipe, patch);
                    return recipeMapper.toResponse(recipeRepository.save(recipe));
                });
    }

    public boolean delete(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) {
            return false;
        }
        recipeRepository.delete(recipe.get());
        return true;
    }
}
