package be.helha.api_recettapp.repositories.jpa;

import be.helha.api_recettapp.models.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing ImageData entities in the database.
 * @author Demba Mohamed Samba
 */
@Repository
public interface ImageDataRepository  extends JpaRepository<ImageData, Integer>, PagingAndSortingRepository<ImageData, Integer> {
    Optional <ImageData> findByName(String Name);

    public void deleteImageDataByName(String Name);
}

