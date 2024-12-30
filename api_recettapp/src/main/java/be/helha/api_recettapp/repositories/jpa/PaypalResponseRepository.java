package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.PaypalResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaypalResponseRepository extends JpaRepository<PaypalResponse, Long> {
}
