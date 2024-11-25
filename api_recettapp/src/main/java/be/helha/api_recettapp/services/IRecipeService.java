package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRecipeService {
    public Page<Recipe> getRecipes(Pageable page);
    public List<Recipe> getRecipes();
    public Recipe addRecipe(Recipe recipe);
    public Recipe updateRecipe(Recipe recipe);
    public void deleteRecipe(int id);
    public Recipe getRecipeById(int id);
}
