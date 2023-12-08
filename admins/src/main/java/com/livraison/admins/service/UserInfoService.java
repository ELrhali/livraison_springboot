package com.livraison.admins.service;

import com.livraison.admins.exceptions.InvalidPasswordException;
import com.livraison.admins.entity.UserInfo;
import com.livraison.admins.repository.UserInfoRepository;
import com.livraison.admins.exceptions.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
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
