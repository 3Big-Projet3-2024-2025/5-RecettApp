package be.helha.api_recettapp.utils;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author Demba Mohamed Samba
 * Utility class for handling data compression and decompression.
 * This class uses annotation {@code @Component} to indicates that this class is a Spring component, eligible for Spring's component scanning.
 */
@Component
public class ImageUtils {
    /**
     *Compresses the given data data using the Deflater compression algorithm.
     * @param data to compress
     * @return {@code byte[]}
     */
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
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

    /**
     * Decompresses the given {@code data} using the {@code Inflater} decompression algorithm.
     * @param data to decompress
     * @return {@code byte[]}
     */
    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }    
}
