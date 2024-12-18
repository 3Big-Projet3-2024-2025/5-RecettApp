package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Contest;
import org.springframework.stereotype.Service;

import java.util.List;

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
