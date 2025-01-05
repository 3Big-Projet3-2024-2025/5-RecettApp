package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Contest.
 * It provides methods to interact with the database.
 * Extends JpaRepository to use built-in CRUD operations.
 */
@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer>, PagingAndSortingRepository<Contest, Integer> {
    /**
     * Find a contest by his title
     * @param title title of the contest
     * @return list a list of contests objects
     */
    public List<Contest> findByTitle(String title);

    /**
     * Get a list of available contest
     * @param pageable a pageable object
     * @return list a list of contests page
     */
    @Query("SELECT c FROM Contest c WHERE c.status = 'true'")
    Page<Contest> findAvailableContests(Pageable pageable);
}
