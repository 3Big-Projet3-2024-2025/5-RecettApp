package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer>, PagingAndSortingRepository<Recipe, Integer> {
    /**
     * Retrieves all recipes that belong to a specific contest.
     *
     * @return A list of recipes associated with the contest.
     * **/
    @Query("SELECT r FROM Recipe r WHERE r.entry.contest.id = :idContest")
    List<Recipe> findRecipesByContestId(@Param("idContest") int idContest);

    /**
     * Retrieves paginated recipes created by a specific user.
     *
     * @param userMail The email address of the user.
     * @param pageable The pagination information.
     * @return A paginated list of recipes.
     */
    @Query("SELECT r FROM Recipe r WHERE r.entry.users.email = :userMail")
    Page<Recipe> findRecipesByUserMail(@Param("userMail") String userMail, Pageable pageable);
    /**
     * Updates the "masked" field of a recipe to the specified value.
     *
     * @param id The ID of the recipe to update.
     * @param masked The new value for the "masked" field.
     */
    @Modifying
    @Query("UPDATE Recipe r SET r.masked = :masked WHERE r.id = :id")
    void updateMasked(@Param("id") int id, @Param("masked") boolean masked);

}