package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an image stored in the system.
 * This class contains metadata and binary data for an image.
 *
 * @author Demba Mohamed Samba
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {

    /**
     * Unique identifier for the image.
     * Automatically generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Name;

    /**
     * The binary data of the image.
     * Stored as a Large Object (LOB) in the database.
     */
    @Lob
    @Column(name = "imagedata")
    private byte[] imageData;

}
