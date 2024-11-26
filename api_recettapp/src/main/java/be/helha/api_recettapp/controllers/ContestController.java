package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.services.IContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * GET - get all the contests
     * @return @type List of all contests
     */
    @GetMapping
    public List<Contest> getContests(){
        return contestService.getContests();
    }

    /**
     * GET - get all the contests by title
     * @param title title to match
     * @return @type List of contests matching the title
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
