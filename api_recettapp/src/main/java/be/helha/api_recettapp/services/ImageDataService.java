package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.ImageData;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import be.helha.api_recettapp.repositories.jpa.ImageDataRepository;
import be.helha.api_recettapp.repositories.jpa.RecipeRepository;
import be.helha.api_recettapp.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.io.IOException;

/**
 * The service methods of ImageData
 */
@Service
public class ImageDataService implements  IImageDataService{

    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;
    /**
     * Adds a new image to the system.
     *
     * @param file the {@link ImageData} object to add.
     * @param recipe the {@link Recipe} associated with the image
     * @return {@code boolean}
     */
    @Override
    public boolean addImageData(MultipartFile file, Recipe recipe) throws IOException {
        try {
            if (file.isEmpty()) {
                recipeRepository.delete(recipe);
                throw new IllegalArgumentException("File is empty");
            }

            // Image type valid: png, jpeg, svg, webp
            List<String> validContentTypes = Arrays.asList("image/jpeg", "image/png", "image/svg", "image/webp");

            if (!validContentTypes.contains(file.getContentType())) {
                recipeRepository.delete(recipe);
                throw new IllegalArgumentException("Invalid file type. Supported types: image/jpeg, image/png, image/svg, image/webp");
            }

            ImageData imageData = imageDataRepository.save(ImageData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .recipe(recipeRepository.getOne(recipe.getId()))
                    .build());

            return imageData.getId() != null;
        } catch (IOException e) {
            recipeRepository.delete(recipe);
            throw new IOException("Failed to process image data: " + e.getMessage(), e);
        } catch (Exception e) {
            recipeRepository.delete(recipe);
            throw new RuntimeException("Unexpected error while adding image data: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves an image.
     *
     * @param nameImage the name of the {@link ImageData} to get.
     * @return {@code byte[] }
     */
    @Override
    @Transactional
    public byte[] getImageData(String nameImage) {
        Optional<ImageData> dbImageData = imageDataRepository.findByName(nameImage);
        if (dbImageData.isEmpty()) {
            throw new RuntimeException("Image " + nameImage +" not found");
        }
        return ImageUtils.decompressImage(dbImageData.get().getImageData());
    }

    /**
     * deletes an image by its name.
     *
     * @param nameImage the name of the image to delete
     * @throws NoSuchElementException if the element is not found
     */
    @Override
    @Transactional
    public void deleteImageData(String nameImage) {
        try {
            this.imageDataRepository.deleteImageDataByName(nameImage);
        }catch (Exception e) {
            throw new NoSuchElementException("Image " + nameImage +" not found" +  "\n Trace : " + e.getMessage());
        }
    }

    /**
     * Add an image evaluation
     * @param file image to add
     * @param evaluation the evaluation object related to the image
     * @return boolean if it works
     * @throws IOException if image is not added
     */
    public boolean addImageEvaluation(MultipartFile file, Evaluation evaluation) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        List<String> validContentTypes = Arrays.asList("image/jpeg", "image/png", "image/svg", "image/webp");

        if (!validContentTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type. Supported types: image/jpeg, image/png, image/svg, image/webp");
        }
        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .evaluation(evaluationRepository.getOne((long) Math.toIntExact(evaluation.getId())))
                .build());
        return imageData.getId() != null;
    }

}
