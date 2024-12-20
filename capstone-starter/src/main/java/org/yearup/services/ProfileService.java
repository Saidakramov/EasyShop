package org.yearup.services;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.repositories.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile updateProfile(Profile profile) {
        Optional<Profile> existingProfile = profileRepository.getProfileByUserId(profile.getUserId());

        // Check if the profile exists before updating
        if (existingProfile.isPresent()) {
            Profile profileToUpdate = existingProfile.get();

            // Update the fields of the existing profile (you can choose to update only specific fields)
            profileToUpdate.setFirstName(profile.getFirstName());
            profileToUpdate.setLastName(profile.getLastName());
            profileToUpdate.setPhone(profile.getPhone());
            profileToUpdate.setEmail(profile.getEmail());
            profileToUpdate.setAddress(profile.getAddress());
            profileToUpdate.setCity(profile.getCity());
            profileToUpdate.setState(profile.getState());
            profileToUpdate.setZip(profile.getZip());

            // Save the updated profile
            return profileRepository.save(profileToUpdate);
        } else {
            // Throw an exception if profile not found
            throw new IllegalArgumentException("Profile with userId " + profile.getUserId() + " not found.");
        }
    }

    public Optional<Profile> getProfileByUserId(int userId) {
        return profileRepository.getProfileByUserId(userId);
    }

    public Profile create(Profile profile) {
        Optional<Profile> existingProfile = profileRepository.getProfileByUserId(profile.getUserId());
        if (existingProfile.isPresent()) {
            throw new IllegalArgumentException("Profile already exists for userId " + profile.getUserId());
        }

        return profileRepository.save(profile);
    }
}
