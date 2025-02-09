package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.EntryRepository;
import be.helha.api_recettapp.models.Entry;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Handle CRUD to handle Entry object with the corresponding JPA Repository
 */
@Service
public class EntryService implements IEntryService {

    @Autowired
    private EntryRepository repository;

    /**
     * Get all entries
     * @return list the list  of entries
     */
    public List<Entry> getEntries() {
        return repository.findAll();
    }

    /**
     * Find an entry by his id
     *
     * @param id int the identifier of the entry
     * @return entry the specific entry
     * @throws EntityNotFoundException if the entry with the id specified is not found
     */
    @Override
    public Entry getEntryById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entry with id " + id + " not found"));
    }

    /**
     *  Find an Entry with a specific UUID and remove it
     *
     * @param uuid the specific uuid
     * @return entry the Entry object
     */
    public Entry removeUuid(UUID uuid){
        Entry entry = repository.findByUuid(uuid);
        if (entry != null) {

            // remove uuid
            entry.setUuid(null);
            return repository.save(entry);
        }


        return null;
    }

    /**
     * Set a UUID to an Entry
     *
     * @param entry the entry to update
     * @return entry  the entry updated
     */
    public Entry setUuid(Entry entry){
        entry.setUuid(UUID.randomUUID());

        return repository.save(entry);
    }

    /**
     * Find all entries for a specific User
     *
     * @param idUser the id of the user
     * @return List users
     */
    @Override
    public List<Entry> getAllEntriesOfUser(long idUser) {
        return repository.findEntriesByUsersId(idUser);
    }

    /**
     * Find an Entry by an user and a contest
     *
     * @param user object user
     * @param contest object contest
     * @return entry object entry
     */
    @Override
    public Entry findByUserAndContest(Users user, Contest contest) {
        return repository.findByUsersAndContest(user, contest);
    }

    /**
     * Find all entries for a specific Contest
     *
     * @param idContest  the id of the contest
     * @return List entries
     */
    @Override
    public List<Entry> getAllEntriesOfContest(Integer idContest) {
        return repository.findEntriesByContestId(idContest);
    }

    /**
     * Find an entry by his UUID
     *
     * @param uuid the specific uuid
     * @return entry the Entry object
     */
    @Override
    public Entry getEntryByUuid(UUID uuid) {
        return repository.findByUuid(uuid);
    }


    /**
     * Add an entry
     * @param entry the entry to add
     * @return entry the entry added
     */
    @Override
    public Entry addEntry(Entry entry) {
        return repository.save(entry);
    }

    /**
     * Update an entry
     * @param entry the entry to update
     * @return entry  the entry updated
     */
    @Override
    public Entry updateEntry(Entry entry) {
        return repository.save(entry);


    }

    /**
     * Delete an entry by his identifier
     * @param id of the entry to delete
     * @throws EntityNotFoundException if there is no entry with the identifier specified in parameter
     */
    @Override
    public void deleteEntry(int id) {
        Entry entry = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entry with id " + id + " not found"));

        repository.deleteById(id);
    }

    /**
     * Get an entry by user email and contest ID
     *
     * @param idContest the ID of the contest
     * @param userMail  the email of the user
     * @return the entry matching the user email and contest ID
     */
    @Override
    public Entry getEntryByUserMailAndIdContest(int idContest, String userMail) {
        return repository.findByContestIdAndUserEmail(idContest, userMail)
                .orElse(null);
    }
}
