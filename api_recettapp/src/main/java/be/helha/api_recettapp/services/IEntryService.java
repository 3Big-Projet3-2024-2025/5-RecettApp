package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Entry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Interface with all the methods implemented in EntryService
 */
public interface IEntryService {
    /**
     * Get all the entries
     * @return @type List<> of entries
     */
    public List<Entry> getEntries();

    /**
     * Get a specific entry
     * @param id the identifier of the entry
     * @return @type List<> of contests matching the title
     */
    public Entry getEntryById(int id);

    /**
     * Get a specific entry by his UUID
     * @param uuid the uuid of the entry
     * @return @type Entry of contests matching the title
     */
    public Entry getEntryByUuid(UUID uuid);

    /**
     * Remove the UUID of a specific entry by his UUID
     * @param uuid the uuid of the entry
     * @return @type Entry of contests matching the title
     */
    public Entry removeUuid(UUID uuid);

    public Entry setUuid(Entry entry);

    /**
     * Add an entry
     * @param entry the entry to add
     * @return @type Entry the entry added
     */
    public Entry addEntry(Entry entry);

    /**
     * Update an entry
     * @param entry the entry to update
     * @return @type Entry updated
     */
    public Entry updateEntry(Entry entry);

    /**
     * Delete an entry
     * @param id the identifier of the entry to delete
     */
    public void deleteEntry(int id);
    /**
     * GET - Get an entry by user email and contest ID
     * @param idContest the ID of the contest
     * @param userMail the email of the user
     * @return the entry matching the user email and contest ID
     */
    public Entry getEntryByUserMailAndIdContest(int idContest, String userMail);
}
