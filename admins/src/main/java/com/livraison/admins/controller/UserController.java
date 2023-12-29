package com.livraison.admins.controller;

import com.livraison.admins.entity.AuthRequest;
import com.livraison.admins.entity.UserInfo;
import com.livraison.admins.exceptions.UserNotFoundException;
import com.livraison.admins.repository.UserInfoRepository;
import com.livraison.admins.service.JwtService;
import com.livraison.admins.service.UserInfoDetails;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class UserController {
    private UserInfoRepository repository;
    @Autowired
    public UserController(UserInfoRepository repository) {
        this.repository = repository;
    }

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
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long userId) {
        return userInfoService.getUserById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserInfo> updateUser(
            @PathVariable Long userId,
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
            @PathVariable Long userId,
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
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
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

    @GetMapping("/test")
    public ResponseEntity<String> testFindByEmail() {
        String username = "momo@gmail.com";
        Optional<UserInfo> userInfoOptional = repository.findByEmail(username);

        if (userInfoOptional.isPresent()) {
            // L'utilisateur a été trouvé
            UserInfo userInfo = userInfoOptional.get();
            String roles = userInfo.getRoles();
            return ResponseEntity.ok("Utilisateur trouvé. Rôles : " + roles);
        } else {
            // L'utilisateur n'a pas été trouvé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé pour l'email : " + username);
        }
    }

    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user using the provided credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // If authentication is successful, generate a JWT token
            if (authentication.isAuthenticated()) {
                UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
                String roles = userDetails.getRole();
                String name = userDetails.getNom();
                Long livreurId = userDetails.getLivreurId();
                Long commercantId = userDetails.getLCommercantId();

                // Utilisez le JwtService pour générer un token avec le rôle
                String token = jwtService.generateToken(authRequest.getUsername(), roles,name,livreurId,commercantId);

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
    //statistic
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalUsers() {
        Long TotalUsers = userInfoService.getTotalUsers();
        return ResponseEntity.ok(TotalUsers);
    }
    @DeleteMapping("/livreur/{id}")
    public ResponseEntity<Void>  deleteUserInfoByLivreurId(@PathVariable  Long id){
        userInfoService.deteletUserInfoByLivreurId(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}