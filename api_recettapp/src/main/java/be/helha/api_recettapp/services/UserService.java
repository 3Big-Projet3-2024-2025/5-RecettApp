package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users findById(Long id) {
        return userRepository.findById((long) Math.toIntExact(id)).orElse(null);
    }

    public Users save(Users user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById((long) Math.toIntExact(id));
    }

}
