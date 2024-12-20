package org.yearup.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yearup.models.User;
import org.yearup.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        return optionalUser.orElse(null);
    }

    public Optional<User> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public int getIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        return userRepository.save(user);
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
