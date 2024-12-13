package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.ImageData;
import be.helha.api_recettapp.repositories.jpa.ImageDataRepository;
import be.helha.api_recettapp.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .Name(file.getOriginalFilename())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());
    }

    /**
     * Retrieves an image.
     *
     * @param nameImage the name of the {@link ImageData} to get.
     * @return {@code byte[] }
     */
    @Override
    public byte[] getImageData(String nameImage) {
        return new byte[0];
    }
}
