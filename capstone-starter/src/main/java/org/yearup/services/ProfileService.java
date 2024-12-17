package org.yearup.services;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.repositories.ProfileRepository;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile) {
        return profileRepository.save(profile);
    }
}
