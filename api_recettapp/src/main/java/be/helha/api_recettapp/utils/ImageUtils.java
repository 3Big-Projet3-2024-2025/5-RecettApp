package be.helha.api_recettapp.utils;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
/**
 * @author Diesbecq Aaron
 * Utility class for handling image compression and decompression.
 * This class uses annotation {@code @Component} to indicates that this class is a Spring component, eligible for Spring's component scanning.
 */
@Component
public class ImageUtils {
    /**
     *Compresses the given image data using the Deflater compression algorithm.
     * @param image to compress
     * @return {@code byte[]}
     */
    public static byte[] compressImage(byte[] image) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(image);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(image.length);
        byte[] buffer = new byte[4*1024];

        while (!deflater.finished()) {
            int size = deflater.deflate(buffer);
            outputStream.write(buffer, 0, size);
        }try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }
}
