package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.services.EntryService;
import be.helha.api_recettapp.services.IEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlers of Entry
 * Handle Entry CRUD
 */
@RestController
@RequestMapping(path="/entries")
public class EntryController {

    @Autowired
    private IEntryService contestService;
    @Autowired
    private EntryService entryService;

    /**
     * GET - get all the entries
     * @return @type List of all entries
     */
    @GetMapping
    public List<Entry> getEntries(){
        return contestService.getEntries();
    }

    /**
     * GET - get a specific entry
     * @param id identifier of the entry
     * @return @type Entry the specific entry
     */
    @GetMapping(path="/{id}")
    public Entry getContestById(@PathVariable int id){
        return contestService.getEntryById(id);
    }

    /**
     * POST - add an entry
     * @param entry the entry to add
     * @return Entry the entry added
     */
    @PostMapping
    public Entry addEntry(@RequestBody Entry entry){
        return contestService.addEntry(entry);
    }

    /**
     * PUT - update an entry
     * @param entry the entry to update
     * @return Entry the entry updated
     */
    @PutMapping
    public Entry updateEntry(@RequestBody Entry entry){
        return contestService.updateEntry(entry);
    }

    /**
     * DELETE - delete an entry by id
     * @param id the identifier of the entry to delete
     */
    @DeleteMapping(path = "/{id}")
    public void deleteEntry(@PathVariable int id){
        contestService.deleteEntry(id);
    }
    /**
     * GET - Get an entry by user email and contest ID
     * @param contestId the ID of the contest
     * @param userMail the email of the user
     * @return Entry the entry matching the user email and contest ID
     */
    @GetMapping("/entry")
    public Entry getEntryByUserMailAndIdContest(
            @RequestParam int contestId,
            @RequestParam String userMail) {
        return entryService.getEntryByUserMailAndIdContest(contestId, userMail);

    }
}
