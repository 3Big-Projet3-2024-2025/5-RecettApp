package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.PaypalResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for PaypalResponseRepository.
 * Provides methods for database operations.
 */
@Repository
public interface PaypalResponseRepository extends JpaRepository<PaypalResponse, Long> {
}
