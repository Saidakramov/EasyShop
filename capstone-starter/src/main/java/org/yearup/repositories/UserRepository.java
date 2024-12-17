package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
