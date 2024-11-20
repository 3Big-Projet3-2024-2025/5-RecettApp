package be.helha.api_recettapp.repositories.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeComponent extends JpaRepository<RecipeComponent, Long> {
    /**
     * Find RecipeComponent by her name
     * @param name
     * @return RecipeComponent
     */
    Optional<RecipeComponent> findByName(String name);
}
