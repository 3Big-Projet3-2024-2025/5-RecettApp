package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.ImageData;
import org.springframework.web.multipart.MultipartFile;


/**
 * Interface for managing ImageData.
 *@author Demba Mohamed Samba
 */
public interface IImageDataService {
    /**
     * Adds a new ingredient to the system.
     *
     * @param file the {@link ImageData} object to add.
     * @return {@code boolean}
     */
    public boolean  addImageData(MultipartFile file);

    /**
     * Retrieves an image.
     *
     * @param nameImage the name of the {@link ImageData} to get.
     * @return {@code byte[] }
     */
    public byte[] getImageData(String nameImage);
}
