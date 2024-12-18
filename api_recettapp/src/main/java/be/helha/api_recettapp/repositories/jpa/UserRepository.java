package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing user entities in the database.
 *
 * This interface extends {@link JpaRepository}, providing basic CRUD operations
 * and additional methods for interacting with {@link Users} entities.
 * The annotation {@code @Repository} is used to indicate that this interface is a Spring Data repository,
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
