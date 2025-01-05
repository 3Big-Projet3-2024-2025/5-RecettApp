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

/**
 * Repository interface for managing Recipe entities in the database.
 * @author Demba Mohamed Samba
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer>, PagingAndSortingRepository<Recipe, Integer> {
    /**
     * Retrieves all recipes that belong to a specific contest.
     *
     * @param idContest the id of the contest
     * @return A list of recipes associated with the contest.
     * **/
    @Query("SELECT r FROM Recipe r WHERE r.masked = false AND r.entry.contest.id = :idContest")
    List<Recipe> findRecipesByContestId(@Param("idContest") int idContest);

    /**
     * Retrieves paginated recipes created by a specific user.
     *
     * @param userMail The email address of the user.
     * @param keyword the term searched
     * @param pageable The pagination information.
     * @return A paginated list of recipes.
     */
        @Query("SELECT r FROM Recipe r WHERE r.masked = false AND r.entry.users.email = :userMail AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        Page<Recipe> findRecipesByUserMailAndKeyword( @Param("userMail") String userMail, @Param("keyword") String keyword, Pageable pageable);

    /**
     * Updates the "masked" field of a recipe to the specified value.
     *
     * @param id The ID of the recipe to update.
     * @param masked The new value for the "masked" field.
     */
    @Modifying
    @Query("UPDATE Recipe r SET r.masked = :masked WHERE r.id = :id")
    void updateMasked(@Param("id") int id, @Param("masked") boolean masked);
    /**
     * Retrieves a paginated list of recipes.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @param keyword the term search.
     * @return a {@link Page} of {@link Recipe} objects.
     */
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Recipe> findByKeyword(@Param("keyword") String keyword, Pageable page);


}