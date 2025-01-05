package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Users;

/**
 * Interface of Users Service
 */
public interface IUserService {
    /**
     * Find a user by his email
     * @param email the email of the user
     * @return users one users object
     */
    Users findByEmail(String email);
}
