package be.helha.api_recettapp.services;

import be.helha.api_recettapp.repositories.jpa.ContestRepository;
import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.repositories.jpa.ContestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


/**
 * Handle CRUD to handle Contest object with the corresponding JPA Repository
 */
@Service
public class ContestService implements IContestService {

    @Autowired
    private ContestRepository repository;

    /**
     * Get all contests
     * @return @type List<>  of contests
     */
    public List<Contest> getContests() {
        return repository.findAll();
    }

    /**
     * Find Contest by his title
     * @param title @type String title of the contest
     * @return @type List of contests matching with the title
     */
    @Override
    public List<Contest> getContestByTitle(String title) {
        return repository.findByTitle(title);
    }

    /**
     * Add a contest
     * @param contest the contest to add
     * @return @type Contest contest added
     * @throws IllegalArgumentException if the contest has missing attributes
     */
    @Override
    public Contest addContest(Contest contest) {
        try{
            if(Contest.checkContest(contest))
                return repository.save(contest);

        } catch (IllegalArgumentException iae){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid add contest data: " + iae.getMessage(), iae
            );
        }
        return null;
    }

    /**
     * Update a contest
     * @param contest the contest to update
     * @return @type Contest contest updated
     * @throws IllegalArgumentException if the contest has missing attributes
     */
    @Override
    public Contest updateContest(Contest contest) {
        try{
            if(Contest.checkContest(contest))
                return repository.save(contest);

        } catch (IllegalArgumentException iae){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid update contest data: " + iae.getMessage(), iae
            );
        }
        return null;
    }

    /**
     * Delete a Contest by his identifier
     * @param id of the contest to delete
     * @throws EntityNotFoundException if there is no Contest with the identifier in parameter
     */
    @Override
    public void deleteContest(int id) {
        Contest contest = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contest with id " + id + " not found"));

        repository.deleteById(id);
    }
}
