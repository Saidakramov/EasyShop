package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.services.ProfileService;
import org.yearup.services.UserService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Optional<Profile>> getProfile(Principal principal) {
        // get the currently logged-in username
        String userName = principal.getName();
        // find database user by userId
        Optional<User> user = userService.getByUserName(userName);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // use the shoppingCartService to get all items in the cart and return the cart
        int userId = user.get().getId();
        Optional<Profile> profile = profileService.getProfileByUserId(userId);

        if (profile.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(Principal principal,
                                                 @RequestBody Profile profile) {
        String userName = principal.getName();
        // find database user by userId
        Optional<User> user = userService.getByUserName(userName);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // use the shoppingCartService to get all items in the cart and return the cart
        int userId = user.get().getId();
        profile.setUserId(userId);
        try {
            Profile updateProfile = profileService.updateProfile(profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(updateProfile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(Principal principal, @RequestBody Profile profile) {
        String userName = principal.getName();

        // Find the database user by userName
        Optional<User> user = userService.getByUserName(userName);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        int userId = user.get().getId();
        profile.setUserId(userId);  // Set the userId on the profile

        try {
            Profile createdProfile = profileService.create(profile);  // Use service method to create profile
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

