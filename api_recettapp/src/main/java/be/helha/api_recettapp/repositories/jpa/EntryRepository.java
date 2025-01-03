package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EntryRepository extends JpaRepository<Entry, Integer> {

    @Query("SELECT e FROM Entry e WHERE e.contest.id = :idContest AND e.users.email = :userMail")
    Optional<Entry> findByContestIdAndUserEmail(@Param("idContest") int idContest, @Param("userMail") String userMail);
}
