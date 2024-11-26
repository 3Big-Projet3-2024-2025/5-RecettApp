package be.helha.api_recettapp.repositories.jpa;
import be.helha.api_recettapp.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer>, PagingAndSortingRepository<Ingredient, Integer> {

    public Ingredient findByAlimentName(String alimentName);
}
