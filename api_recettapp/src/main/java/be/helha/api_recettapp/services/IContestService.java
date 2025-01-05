package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface with all the methods implemented in ContestService
 */
public interface IContestService {

    /**
     * Get a paginated list of contests
     *
     * @param page the object containing pagination information
     * @return List of Page objects
     */
    public Page<Contest> getContests(Pageable page);

    /**
     * Get a paginated list of available contests
     *
     * @param page the object containing pagination information
     * @return List of Page objects
     */
    public Page<Contest> getAvailableContests(Pageable page);

    /**
     * Get all the contests
     * @return  list of contests
     */
    public List<Contest> getContests();
    /**
     * Get contests matching a title
     * @param title the title of the contest to match
     * @return list of contests matching the title
     */
    public List<Contest> getContestByTitle(String title);

    /**
     * Get contest matching an id
     *
     * @param id of the contest to match
     * @return optionnal of contest matching the title
     */
    public Optional<Contest> getContestById(int id);

    /**
     * Add a contest
     * @param contest the contest to add
     * @return contest the contest added
     */
    public Contest addContest(Contest contest);
    /**
     * Update a contest
     * @param contest the contest to update
     * @return contest the contest updated
     */
    public Contest updateContest(Contest contest);
    /**
     * Delete a contest
     * @param id the identifier of the contest to delete
     */
    public void deleteContest(int id);
}
