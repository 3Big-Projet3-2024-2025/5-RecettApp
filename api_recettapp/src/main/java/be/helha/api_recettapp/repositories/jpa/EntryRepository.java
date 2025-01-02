package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface EntryRepository extends JpaRepository<Entry, Integer> {
    /*
    Find an Entry by his UUID
    @param uiid the universal unique identifier
     */
    Entry findByUuid(UUID uuid);
}
