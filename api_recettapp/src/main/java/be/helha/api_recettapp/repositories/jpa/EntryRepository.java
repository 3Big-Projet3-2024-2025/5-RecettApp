package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

/**
 * Repository interface for managing {@link Entry} entities.
 * Provides methods to perform CRUD operations and custom queries on Entry data.
 */
@Repository
public interface EntryRepository extends JpaRepository<Entry, Integer> {

    /**
     * Finds an Entry by its UUID.
     *
     * @param uuid the universal unique identifier of the Entry.
     * @return the Entry associated with the given UUID.
     */
    Entry findByUuid(UUID uuid);

    /**
     * Finds all Entries associated with a specific Contest ID.
     *
     * @param idContest the ID of the Contest.
     * @return a list of Entries linked to the given Contest ID.
     */
    List<Entry> findEntriesByContestId(Integer idContest);

    /**
     * Finds all Entries associated with a specific User ID.
     *
     * @param idUsers the ID of the User.
     * @return a list of Entries linked to the given User ID.
     */
    List<Entry> findEntriesByUsersId(long idUsers);

    /**
     * Finds an Entry by a specific User and Contest.
     *
     * @param user the User associated with the Entry.
     * @param contest the Contest associated with the Entry.
     * @return the Entry associated with the given User and Contest.
     */
    Entry findByUsersAndContest(Users user, Contest contest);

    /**
     * Finds an Entry by Contest ID and User email.
     *
     * @param idContest the ID of the Contest.
     * @param userMail the email of the User.
     * @return an Optional containing the Entry if found, or empty otherwise.
     */
    @Query("SELECT e FROM Entry e WHERE e.contest.id = :idContest AND e.users.email = :userMail")
    Optional<Entry> findByContestIdAndUserEmail(@Param("idContest") int idContest, @Param("userMail") String userMail);

}
