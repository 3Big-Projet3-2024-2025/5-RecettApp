package be.helha.api_recettapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Error message model
 */
@Data
@AllArgsConstructor
public class AppError {

    private String message;
}