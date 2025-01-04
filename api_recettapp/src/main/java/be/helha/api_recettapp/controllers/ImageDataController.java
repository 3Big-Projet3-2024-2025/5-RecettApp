package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Evaluation;
import be.helha.api_recettapp.models.ImageData;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.repositories.jpa.EvaluationRepository;
import be.helha.api_recettapp.repositories.jpa.ImageDataRepository;
import be.helha.api_recettapp.repositories.jpa.RecipeRepository;
import be.helha.api_recettapp.services.ImageDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller class for managing image data.
 * @author Demba Mohamed Samba
 */
@RestController
@RequestMapping(path="/image")
public class ImageDataController {

    @Autowired
    private ImageDataService imageDataService;
    @Autowired
    private ImageDataRepository repository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;


    /**
     * Uploads a new image to the server.
     *
     * @param image the image file to upload and the {@code Recipe ID} , provided as a {@link MultipartFile}.
     * @return a {@link ResponseEntity} indicating the success or failure of the upload operation.
     * @throws IOException if an error occurs during file processing.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile image, @RequestParam("recipe") Long recipeId) throws IOException {

        Recipe recipe = recipeRepository.findById(Math.toIntExact(recipeId))
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found with ID: " + recipeId));

        boolean uploadImage = imageDataService.addImageData(image, recipe);
        if (!uploadImage) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Retrieves an image by its name.
     *
     * @param fileName the name of the image to retrieve.
     * @return a {@link ResponseEntity} containing the image data and the appropriate content type.
     * @throws RuntimeException if the image is not found or the type is invalid.
     */
    @GetMapping("/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) {
        byte[] imageData = imageDataService.getImageData(fileName);
        ImageData image = repository.findByName(fileName).get();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(image.getType()))
                .body(imageData);
    }

    @PostMapping(value = "/{evaluation}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addImageEvaluation(@RequestParam("image") MultipartFile image, @RequestParam("evaluation") int evaluationId) throws IOException {

        Evaluation evaluation = evaluationRepository.findById((long) evaluationId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found with ID: " + evaluationId));

        boolean uploadImage = imageDataService.addImageEvaluation(image,evaluation);
        if (!uploadImage) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
