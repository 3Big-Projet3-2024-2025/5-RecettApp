package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository {
    Optional<Recipe> findByTitle(String title);
}
