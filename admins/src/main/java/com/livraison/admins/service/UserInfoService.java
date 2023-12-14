package com.livraison.admins.service;

import com.livraison.admins.exceptions.InvalidPasswordException;
import com.livraison.admins.entity.UserInfo;
import com.livraison.admins.repository.UserInfoRepository;
import com.livraison.admins.exceptions.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = repository.findByEmail(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
    public UserInfo updateUser(int userId, String currentPassword, String newPassword, UserInfo updatedUserInfo) {
        Optional<UserInfo> existingUser = repository.findById(userId);

        if (existingUser.isPresent()) {
            UserInfo userToUpdate = existingUser.get();

            // Check if the provided current password matches the stored password
            if (passwordEncoder.matches(currentPassword, userToUpdate.getPassword())) {
                // Update the password
                userToUpdate.setPassword(passwordEncoder.encode(newPassword));

                // Update other user information
                userToUpdate.setName(updatedUserInfo.getName());
                userToUpdate.setEmail(updatedUserInfo.getEmail());
                userToUpdate.setRoles(updatedUserInfo.getRoles());

                return repository.save(userToUpdate);
            } else {
                // Password does not match, handle authentication failure
                throw new InvalidPasswordException("Invalid current password for user with ID: " + userId);
            }
        } else {
            // Handle user not found
            throw new UserNotFoundException("User not found");
        }
    }

    public String addUser(UserInfo userInfo) {
        try {
            // Check if the name or email already exists before adding a user
            if (repository.existsByName(userInfo.getName())) {
                return "Error adding user: Name already exists";
            }

            if (repository.existsByEmail(userInfo.getEmail())) {
                return "Error adding user: Email already exists";
            }

            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            repository.save(userInfo);
            return "User Added Successfully";
        } catch (DataIntegrityViolationException e) {
            // This exception will be thrown if the unique constraint is violated
            return "Error adding user: Name or email already exists";
        } catch (Exception e) {
            return "Error adding user: " + e.getMessage();
        }
    }

    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }
    // Get user by ID
    public Optional<UserInfo> getUserById(int userId) {
        return repository.findById(userId);
    }
    public void deleteUser(int userId) {
        repository.deleteById(userId);
    }
    public void validateToken(String token) {
        try {
            jwtService.validateToken(token);
        } catch (JwtException e) {
            // Gérez l'exception liée au JWT ici
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
    public UserInfo updatePassword(int userId, String currentPassword, String newPassword) {
        Optional<UserInfo> existingUser = repository.findById(userId);

        if (existingUser.isPresent()) {
            UserInfo userToUpdate = existingUser.get();

            // Check if the provided current password matches the stored password
            if (passwordEncoder.matches(currentPassword, userToUpdate.getPassword())) {
                // Update the password
                userToUpdate.setPassword(passwordEncoder.encode(newPassword));
                return repository.save(userToUpdate);
            } else {
                // Password does not match, handle authentication failure
                throw new InvalidPasswordException("Invalid current password");
            }
        } else {
            // Handle user not found
            throw new UserNotFoundException("User not found");
        }
    }

    // Reset password for a user (in case of forgotten password)
    public UserInfo resetPassword(String email, String newPassword) {
        Optional<UserInfo> existingUser = repository.findByEmail(email);

        if (existingUser.isPresent()) {
            UserInfo userToUpdate = existingUser.get();
            // Update the password
            userToUpdate.setPassword(passwordEncoder.encode(newPassword));
            return repository.save(userToUpdate);
        } else {
            // Handle user not found
            throw new UserNotFoundException("User not found");
        }
    }
}
