package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.services.IContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlers of Contest
 * Handle Contest CRUD
 */
@RestController
@RequestMapping(path="/contests")
public class ContestController {

    @Autowired
    private IContestService contestService;


    /**
     * GET - get a paginated list of contests
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link Recipe} objects.
     */
    @GetMapping
    public Page<Contest> getContests(@PageableDefault(page = 0, size = 10) Pageable page){
        try {

            return contestService.getContests(page);
        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error retrieving contests pages"
            );
        }
    }

    /**
     * GET - get a paginated list of available contests
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link Recipe} objects.
     */
    @GetMapping("/availableContests")
    public Page<Contest> getAvailableContests(@PageableDefault(page = 0, size = 10) Pageable page){
        try {

            return contestService.getAvailableContests(page);
        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error retrieving available contests pages"
            );
        }
    }

    /**
     * GET - get all the contests
     * @return list List of all contests
     */
    @GetMapping("/all")
    public List<Contest> getContests(){
        return contestService.getContests();
    }

    /**
     * GET - get all the contests by title
     * @param title title to match
     * @return list of contests matching the title
     */
    @GetMapping(path="/{title}")
    public List<Contest> getContestByName(@PathVariable String title){
        return contestService.getContestByTitle(title);
    }

    /**
     * POST - add a contest
     * @param contest the contest to add
     * @return Contest the contest added
     */
    @PostMapping
    public Contest addContest(@RequestBody Contest contest){
        return contestService.addContest(contest);
    }

    /**
     * PUT - update a contest
     * @param contest the contest to update
     * @return Contest the contest updated
     */
    @PutMapping
    public Contest updateContest(@RequestBody Contest contest){
        return contestService.updateContest(contest);
    }

    /**
     * DELETE - delete a contest by id
     * @param id the identifier of the contest to delete
     */
    @DeleteMapping(path = "/{id}")
    public void deleteContest(@PathVariable int id){
        contestService.deleteContest(id);
    }
}
