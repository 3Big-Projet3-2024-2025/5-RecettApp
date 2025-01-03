package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.services.EntryService;
import be.helha.api_recettapp.services.IEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Controlers of Entry
 * Handle Entry CRUD
 */
@RestController
@RequestMapping(path="/entries")
public class EntryController {

    @Autowired
    private IEntryService entryService;


    /**
     * GET - get all the entries
     * @return @type List of all entries
     */
    @GetMapping
    public List<Entry> getEntries(){
        return entryService.getEntries();
    }

    /**
     * GET - get a specific entry
     * @param id identifier of the entry
     * @return @type Entry the specific entry
     */
    @GetMapping(path="/{id}")
    public Entry getContestById(@PathVariable int id){
        return entryService.getEntryById(id);
    }

    /**
     * POST - add an entry
     * @param entry the entry to add
     * @return Entry the entry added
     */
    @PostMapping
    public Entry addEntry(@RequestBody Entry entry){
        return entryService.addEntry(entry);
    }

    /**
     * PUT - update an entry
     * @param entry the entry to update
     * @return Entry the entry updated
     */
    @PutMapping
    public Entry updateEntry(@RequestBody Entry entry){
        return entryService.updateEntry(entry);
    }


    /**
     * PUT - register an entry if the UUID is correct
     * @param entry the entry to update
     * @param uuid the uuid of the entry registered
     * @return Entry the entry registered
     */
    @PutMapping("/register")
    public Entry registerEntry(@RequestBody Entry entry, @RequestBody UUID uuid){
        Entry entry1 = entryService.getEntryById(entry.getId());


        if(entry1.getUuid() == uuid){
            entry1.setStatus("registered");
            entry1.setUuid(null);
            entryService.updateEntry(entry);
            return entry;
        } else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The provided UUID is incorrect or not found for this entry."
            );
        }

    }

    /**
     * DELETE - delete an entry by id
     * @param id the identifier of the entry to delete
     */
    @DeleteMapping(path = "/{id}")
    public void deleteEntry(@PathVariable int id){
        entryService.deleteEntry(id);
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
