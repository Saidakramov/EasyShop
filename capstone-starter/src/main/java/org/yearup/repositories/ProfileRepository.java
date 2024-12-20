package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.Profile;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
   Optional<Profile> getProfileByUserId(int userId);
}
