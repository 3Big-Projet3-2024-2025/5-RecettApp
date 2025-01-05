package be.helha.api_recettapp.services;

import be.helha.api_recettapp.repositories.jpa.ContestRepository;
import be.helha.api_recettapp.models.Contest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;


/**
 * Handle CRUD to handle Contest object with the corresponding JPA Repository
 */
@Service
public class ContestService implements IContestService {

    @Autowired
    private ContestRepository repository;

    /**
     * Get a pagination of Contests
     *
     * @param page the object containing pagination information
     * @return page the page of contests
     */
    @Override
    public Page<Contest> getContests(Pageable page) {
            return repository.findAll(page);
    }

    /**
     * @param page the object containing pagination information
     * @return the page of available contests
     */
    @Override
    public Page<Contest> getAvailableContests(Pageable page) {
        return repository.findAvailableContests(page);
    }

    /**
     * Get all contests
     * @return list of contests
     */
    public List<Contest> getContests() {
        return repository.findAll();
    }

    /**
     * Find Contest by his title
     * @param title String title of the contest
     * @return list of contests matching with the title
     */
    @Override
    public List<Contest> getContestByTitle(String title) {
        return repository.findByTitle(title);
    }

    /**
     * Find a contest by his id
     *
     * @param id of the contest to match
     * @return optional an optionnal of a contest
     */
    @Override
    public Optional<Contest> getContestById(int id) {
        return repository.findById(id);
    }

    /**
     * Add a contest
     * @param contest the contest to add
     * @return contest contest added
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
     * @return  contest the contest updated
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
