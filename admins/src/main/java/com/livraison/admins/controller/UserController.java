package com.livraison.admins.controller;

import com.livraison.admins.entity.AuthRequest;
import com.livraison.admins.entity.UserInfo;
import com.livraison.admins.exceptions.UserNotFoundException;
import com.livraison.admins.service.JwtService;
import com.livraison.admins.service.UserInfoService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/generateToken/validate")
    public String validateToken(@RequestParam("token") String token) {
        try {
            userInfoService.validateToken(token );
            return "Token is valid";
        } catch (JwtException e) {
            // Log the details of the exception
            e.printStackTrace();
            // You can also log specific information like e.getMessage() or e.getCause()
            return "Invalid token";
        }
    }
    @GetMapping("/validation")
    public String validationT(@RequestParam("token") String token)
    {try {
        jwtService.extractUsername(token);
        return "true";
    }catch (Exception e){
        return "false";
    }
    }
    // Get all users
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserInfo> users = userInfoService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable int userId) {
        return userInfoService.getUserById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserInfo> updateUser(
            @PathVariable int userId,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestBody UserInfo updatedUserInfo
    ) {
        try {
            UserInfo updatedUser = userInfoService.updateUser(userId, currentPassword, newPassword, updatedUserInfo);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Update user password
    @PutMapping("/{userId}/update-password")
    public ResponseEntity<UserInfo> updatePassword(
            @PathVariable int userId,
            @RequestParam String currentPassword,
            @RequestParam String newPassword
    ) {
        try {
            UserInfo updatedUser = userInfoService.updatePassword(userId, currentPassword, newPassword);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/welcome")
    public String welcomea() {
        return "Welcome this endpoint is not hh";
    }

    @PostMapping("/newuser")
    public ResponseEntity<String> createUser(@RequestBody UserInfo userInfo) {
        try {
            String result = userInfoService.addUser(userInfo);
            if (result.equals("User Added Successfully")) {
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userInfoService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('admin')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }


    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user using the provided credentials
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // If authentication is successful, generate a JWT token
            if (authentication.isAuthenticated()) {
                // Use the JwtService to generate a token for the authenticated user
                String token = jwtService.generateToken(authRequest.getUsername());

                // Return the generated token
                return ResponseEntity.ok(token);
            } else {
                // If authentication fails, return a 401 Unauthorized response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            // Handle authentication exceptions, such as bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

}