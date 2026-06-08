package com.example.spring_demo.controllers;

import com.example.spring_demo.dto.*;
import com.example.spring_demo.exception.ApiErrorResponse;
import com.example.spring_demo.services.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeResponse> getAllRecipes(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "ingredients", required = false) String ingredients
    ) {
        return recipeService.findAll(name, ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable("id") Long id) {
        return recipeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody @Valid RecipeCreateRequest request) {
        RecipeResponse created = recipeService.create(request);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable("id") Long id,
            @RequestBody @Valid RecipeUpdateRequest request
    ) {
        return recipeService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchRecipe(@PathVariable("id") Long id, @RequestBody @Valid RecipePatchRequest patch) {
        if (patch.hasNoUpdates()) {
            return badRequest(null, "At least one field (name, ingredients, instructions) must be provided");
        }
        if (patch.getIngredients() != null && patch.getIngredients().isBlank()) {
            return badRequest("ingredients", "Ingredients cannot be blank");
        }
        if (patch.getInstructions() != null && patch.getInstructions().isBlank()) {
            return badRequest("instructions", "Instructions cannot be blank");
        }

        return recipeService.patch(id, patch)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") Long id) {
        if (recipeService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<ApiErrorResponse> badRequest(String field, String message) {
        List<ApiErrorResponse.FieldError> errors = field == null
                ? List.of()
                : List.of(new ApiErrorResponse.FieldError(field, message));
        String summary = field == null ? message : "Validation failed";
        ApiErrorResponse error = new ApiErrorResponse(400, summary, errors);
        return ResponseEntity.badRequest().body(error);
    }
}
