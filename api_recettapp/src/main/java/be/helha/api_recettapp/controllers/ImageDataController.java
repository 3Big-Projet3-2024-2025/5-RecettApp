package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.services.ImageDataService;
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


    /**
     * Uploads a new image to the server.
     *
     * @param image the image file to upload, provided as a {@link MultipartFile}.
     * @return a {@link ResponseEntity} indicating the success or failure of the upload operation.
     * @throws IOException if an error occurs during file processing.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile image ) throws IOException {
        boolean uploadImage = imageDataService.addImageData(image);
        if (!uploadImage) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Image added successfully");
    }

}
