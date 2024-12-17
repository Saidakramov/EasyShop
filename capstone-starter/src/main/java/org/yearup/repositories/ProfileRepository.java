package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
