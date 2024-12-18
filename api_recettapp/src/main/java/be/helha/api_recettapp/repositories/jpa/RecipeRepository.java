package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer>, PagingAndSortingRepository<Recipe, Integer> {

    public List<Recipe> findByTitle(String title);
}