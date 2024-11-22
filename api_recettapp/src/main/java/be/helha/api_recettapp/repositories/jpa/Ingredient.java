package be.helha.api_recettapp.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Ingredient extends JpaRepository<Ingredient, Long> {
    /**
     * Find on ingredient by her name
     * @param name
     * @return Ingredient
     */
    Ingredient findByName(String name);
}
