package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * Retrieves all recipes created by a specific user.
     *
     * @param userMail The email address of the user.
     * @return A list of recipes created by the user.
     */
    @Query("SELECT r FROM Recipe r WHERE r.entry.users.email = :userMail")
    List<Recipe> findRecipesByUserMail(@Param("userMail") String userMail);
}