package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.services.IRecipeService;
import be.helha.api_recettapp.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController {

    @Autowired
    private IRecipeService RecipeService;

    @GetMapping
    public Page<Recipe> getRecipe(Pageable p) {
        Page recipes = RecipeService.getRecipes(p);
        return recipes;
    }

    @PostMapping
    public Recipe addRecipe(Recipe recipe) {
        return RecipeService.addRecipe(recipe);
    }

}
