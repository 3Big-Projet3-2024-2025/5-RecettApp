package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.repositories.jpa.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RecipeService  implements IRecipeService{

    @Autowired
    private RecipeRepository recipeRepository;
    /**
     * @param page
     * @return
     */
    @Override
    public Page<Recipe> getRecipes(Pageable page) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<Recipe> getRecipes() {
        return List.of();
    }

    /**
     * @param recipe
     * @return
     */
    @Override
    public Recipe addRecipe(Recipe recipe) {
        return null;
    }

    /**
     * @param recipe
     * @return
     */
    @Override
    public Recipe updateRecipe(Recipe recipe) {
        return null;
    }

    /**
     * @param id
     */
    @Override
    public void deleteRecipe(int id) {

    }

    /**
     * @param id
     * @return
     */
    @Override
    public Recipe getRecipeById(int id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("RecipeComponent with id " + id + " not found"));
    }
}
