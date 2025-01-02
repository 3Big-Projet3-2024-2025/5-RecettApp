package be.helha.api_recettapp.services;

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
     * @return @type List<>  of entries
     */
    public List<Entry> getEntries() {
        return repository.findAll();
    }

    /**
     * Find an entry by his id
     *
     * @param id @type int the identifier of the entry
     * @return @type Entry the specific entry
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
            repository.save(entry);
        }


        return null;
    }

    /**
     * Set a UUID to an Entry
     *
     * @param entry the entry to update
     * @return @type Entry the entry updated
     */
    public Entry setUuid(Entry entry){
        entry.setUuid(UUID.randomUUID());

        return repository.save(entry);
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
     * @return @type Entry the entry added
     */
    @Override
    public Entry addEntry(Entry entry) {
        return repository.save(entry);
    }

    /**
     * Update an entry
     * @param entry the entry to update
     * @return @type Entry the entry updated
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
}
