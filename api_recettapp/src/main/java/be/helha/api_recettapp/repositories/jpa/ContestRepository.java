package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
    public List<Contest> findByTitle(String title);
}
