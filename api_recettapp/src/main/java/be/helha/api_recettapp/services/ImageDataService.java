package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.ImageData;
import be.helha.api_recettapp.repositories.jpa.ImageDataRepository;
import be.helha.api_recettapp.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.io.IOException;

@Service
public class ImageDataService implements  IImageDataService{

    @Autowired
    private ImageDataRepository imageDataRepository;
    /**
     * Adds a new ingredient to the system.
     *
     * @param file the {@link ImageData} object to add.
     * @return {@code boolean}
     */
    @Override
    public boolean addImageData(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        //Image type valid: png, jpeg, svg, webp
        List<String> validContentTypes = Arrays.asList("image/jpeg", "image/png", "image/svg", "image/webp");

        if (!validContentTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type. Supported types: image/jpeg, image/png, image/svg, image/webp");
        }
        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());
        return imageData.getId() != null;
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

}
