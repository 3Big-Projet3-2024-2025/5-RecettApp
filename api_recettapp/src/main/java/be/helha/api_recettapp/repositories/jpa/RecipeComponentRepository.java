package be.helha.api_recettapp.repositories.jpa;


import be.helha.api_recettapp.models.RecipeComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeComponentRepository  extends JpaRepository<RecipeComponent, Integer>, PagingAndSortingRepository<RecipeComponent, Integer> {
    boolean existsByRecipeIdAndIngredientId(int recipeId, int ingredientId);
}
