package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }
}
