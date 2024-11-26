package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.repositories.jpa.ContestCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing ContestCategory.
 * This class contains the business logic
 * contest categories and interacts with the repository.
 */
@Service
public class ContestCategoryService {

    @Autowired
    private ContestCategoryRepository repository;

    /**
     * Shows all contest categories from the database.
     *
     * @return a list of all contest categories.
     */
    public List<ContestCategory> findAll() {
        return repository.findAll();
    }

    /**
     * Find categories by title.
     * This method retrieves all categories and filters them by the given title.
     *
     * @param title the title to search for.
     * @return a list of matching contest categories.
     */
    public List<ContestCategory> findByTitle(String title) {
        // Retrieve all categories and filter by title (case-insensitive, partial match)
        return repository.findAll().stream()
                .filter(category -> category.getTitle() != null &&
                        category.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Find a specific contest category by its ID.
     *
     * @param id the ID of the category.
     * @return an Optional containing the category if found, or empty if not.
     */
    public Optional<ContestCategory> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Save a new contest category or update an existing one.
     *
     * @param category the contest category to save or update.
     * @return the saved contest category.
     */
    public ContestCategory save(ContestCategory category) {
        return repository.save(category);
    }

    /**
     * Delete a contest category by its ID.
     *
     * @param id the ID of the category to delete.
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
