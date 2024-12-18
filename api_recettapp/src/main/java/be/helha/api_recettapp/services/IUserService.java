package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Users;

public interface IUserService {
    Users findByEmail(String email);
}
