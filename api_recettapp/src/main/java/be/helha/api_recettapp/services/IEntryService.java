package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Interface with all the methods implemented in EntryService
 */
public interface IEntryService {
    /**
     * Get all the entries
     * @return list the list of entries
     */
    public List<Entry> getEntries();

    /**
     * Get a specific entry
     * @param id the identifier of the entry
     * @return entry the entry  matching the id
     */
    public Entry getEntryById(int id);

    /**
     * Get a specific entry by his UUID
     * @param uuid the uuid of the entry
     * @return entry list of  entries matching the title
     */
    public Entry getEntryByUuid(UUID uuid);

    /**
     * Remove the UUID of a specific entry by his UUID
     * @param uuid the uuid to remove
     * @return entry the entry matching the UUID
     */
    public Entry removeUuid(UUID uuid);

    /**
     * Set a UUID on a specific entry
     * @param entry the entry
     * @return entry the entry object matching the entry
     */
    public Entry setUuid(Entry entry);

    /**
     * Get all the entries of an User
     *
     * @param idUser the id of the user
     * @return list A list of entries of the user
     */
    public List<Entry> getAllEntriesOfUser(long idUser);

    /**
     * Find an Entry by an user and contest object
     * @param user an user object
     * @param contest a contest object
     * @return entru  an entry object corresponding to the user and the contest
     */
    public Entry findByUserAndContest(Users user, Contest contest);

    /**
     * Get all the entries for an id of a contest
     * @param idContest the id of the contest
     * @return list a list of entries for the contest
     */
    public List<Entry> getAllEntriesOfContest(Integer idContest);

    /**
     * Add an entry
     * @param entry the entry to add
     * @return entry the entry added
     */
    public Entry addEntry(Entry entry);

    /**
     * Update an entry
     * @param entry the entry to update
     * @return entry the entry updated
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
