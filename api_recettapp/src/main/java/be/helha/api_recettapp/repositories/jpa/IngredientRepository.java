package be.helha.api_recettapp.repositories.jpa;
import be.helha.api_recettapp.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Ingredient entities in the database.
 * @author Demba Mohamed Samba
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer>, PagingAndSortingRepository<Ingredient, Integer> {

    /**
     * Find an aliment by his name
     * @param alimentName the aliment name
     * @return ingredient a ingredient object
     */
    public Ingredient findByAlimentName(String alimentName);
}
