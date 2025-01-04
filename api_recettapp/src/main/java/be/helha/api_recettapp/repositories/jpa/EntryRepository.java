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



@Repository
public interface EntryRepository extends JpaRepository<Entry, Integer> {

    /*
    Find an Entry by his UUID
    @param uiid the universal unique identifier
     */
    Entry findByUuid(UUID uuid);
    List<Entry> findEntriesByContestId(Integer idContest);
    List<Entry> findEntriesByUsersId(long idUsers);
    Entry findByUsersAndContest(Users user, Contest contest);
    @Query("SELECT e FROM Entry e WHERE e.contest.id = :idContest AND e.users.email = :userMail")
    Optional<Entry> findByContestIdAndUserEmail(@Param("idContest") int idContest, @Param("userMail") String userMail);

}
