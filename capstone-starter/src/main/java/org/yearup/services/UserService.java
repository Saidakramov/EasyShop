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

    public List<User> getAll() {
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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return user.getId();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public boolean exists(String username) {
        return userRepository.findByUsername(username).orElse(null) == null ? false: true;
    }
}
