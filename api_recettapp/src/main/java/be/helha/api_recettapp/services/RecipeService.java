package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService  implements IRecipeService{
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
}
