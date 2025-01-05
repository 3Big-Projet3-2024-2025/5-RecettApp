package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.ImageData;
import be.helha.api_recettapp.models.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;


/**
 * Interface for managing ImageData.
 *@author Demba Mohamed Samba
 */
public interface IImageDataService {
    /**
     * Adds a new image to the system.
     *
     * @param file the {@link ImageData} object to add.
     * @param recipe the {@link Recipe} associated with the image
     * @return {@code boolean}
     * @throws IOException throws IOException
     */
    public boolean  addImageData(MultipartFile file, Recipe recipe) throws IOException;

    /**
     * Retrieves an image.
     *
     * @param nameImage the name of the {@link ImageData} to get.
     * @return {@code byte[] }
     */
    public byte[] getImageData(String nameImage);

    /**
     * deletes an image by its name.
     *
     * @param nameImage the name of the image to delete
     * @throws NoSuchElementException if there is no image
     */
    public void deleteImageData(String nameImage) throws NoSuchElementException;
}
