package be.helha.api_recettapp.repositories.jpa;


import be.helha.api_recettapp.models.RecipeComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing RecipeComponent entities in the database.
 * @author Demba Mohamed Samba
 */
@Repository
public interface RecipeComponentRepository  extends JpaRepository<RecipeComponent, Integer>, PagingAndSortingRepository<RecipeComponent, Integer> {

    /**
     * Find if a recipe and an ingredient exist
     *
     * @param recipeId the id of the recipe
     * @param ingredientId the id of the ingredient
     * @return boolean
     */
    boolean existsByRecipeIdAndIngredientId(int recipeId, int ingredientId);
}
