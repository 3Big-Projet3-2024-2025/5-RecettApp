package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Contest;

import java.util.List;
import java.util.Optional;

/**
 * Interface with all the methods implemented in ContestService
 */
public interface IContestService {
    /**
     * Get all the contests
     * @return @type List<> of contests
     */
    public List<Contest> getContests();
    /**
     * Get contests matching a title
     * @param title the title of the contest to match
     * @return @type List<> of contests matching the title
     */
    public List<Contest> getContestByTitle(String title);

    /**
     * Get contest matching an id
     *
     * @param id of the contest to match
     * @return @type Contest of contests matching the title
     */
    public Optional<Contest> getContestById(int id);

    /**
     * Add a contest
     * @param contest the contest to add
     * @return @type Contest the contest added
     */
    public Contest addContest(Contest contest);
    /**
     * Update a contest
     * @param contest the contest to update
     * @return @type Contest the contest update
     */
    public Contest updateContest(Contest contest);
    /**
     * Delete a contest
     * @param id the identifier of the contest to delete
     */
    public void deleteContest(int id);
}
