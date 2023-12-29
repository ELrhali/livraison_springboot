package com.livraison.admins.service;

import com.livraison.admins.dto.CommercantsDto;
import com.livraison.admins.dto.LivreurDto;
import com.livraison.admins.exceptions.InvalidPasswordException;
import com.livraison.admins.entity.UserInfo;
import com.livraison.admins.repository.UserInfoRepository;
import com.livraison.admins.exceptions.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RestTemplate restTemplate;

    // @Override
   /* public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = repository.findByEmail(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }*/
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<UserInfo> userDetail = repository.findByEmail(username);

       return userDetail.map(userInfo -> {
           String roles = retrieveRolesByUsername(username);
           String nom = retrieveNomByUsername(username);
           return new UserInfoDetails(userInfo, roles);
       }).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
   }


    public String retrieveRolesByUsername(String username) {
        Optional<UserInfo> userInfoOptional = repository.findByEmail(username);
        return userInfoOptional.map(UserInfo::getRoles).orElse("");
    }
    public String retrieveNomByUsername(String username) {
        Optional<UserInfo> userInfoOptional = repository.findByEmail(username);
        return userInfoOptional.map(UserInfo::getName).orElse("");
    }
    public UserInfo updateUser(Long userId, String currentPassword, String newPassword, UserInfo updatedUserInfo) {
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
            if (repository.existsByEmail(userInfo.getEmail())) {
                return "Error adding user: Email already exists";
            }

            String role = userInfo.getRoles();

            if ("admin".equals(role)) {
                // Handle admin logic
                userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
                repository.save(userInfo);
            } else if ("livreur".equals(role)) {
                // Handle livreur logic
                Long livreurId = getLivreurId(userInfo.getEmail());
                if (livreurId != null) {
                    userInfo.setLivreurId(livreurId);
                    userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
                    repository.save(userInfo);
                } else {
                    return "Error: No livreur found for the provided email";
                }
            } else if ("user".equals(role)) {
                // Handle commercant logic
                CommercantsDto commercantsDto = new CommercantsDto();
                commercantsDto.setCIN(userInfo.getCIN());
                commercantsDto.setNom(userInfo.getName());
                commercantsDto.setPrenom(userInfo.getPrenom());
                commercantsDto.setEmail(userInfo.getEmail());
                commercantsDto.setAddrese(userInfo.getAddrese());
                commercantsDto.setPhone(userInfo.getPhone());

                // ... set other properties

                // Call your Commercants service to save the CommercantsDto
                String commercantServiceUrl = "http://localhost:8087/api/commercants";
                ResponseEntity<CommercantsDto> commercantResponse = restTemplate.postForEntity(
                        commercantServiceUrl,
                        commercantsDto,
                        CommercantsDto.class
                );
                Long commercantId = Objects.requireNonNull(commercantResponse.getBody()).getId();

                // Set the commercantId in the UserInfo entity
                userInfo.setCommercantId(commercantId);

                userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
                repository.save(userInfo);

            } else {
                return "Error: Invalid role provided";
            }
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
        return "User Added Successfully";
    }
    private Long getLivreurId(String email) {
        String livreurServiceUrl = "http://localhost:8081/api/livreurs/livreur-id";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(livreurServiceUrl)
                .queryParam("email", email);

        ResponseEntity<Long> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                Long.class
        );

        return responseEntity.getBody();
    }

    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }
    // Get user by ID
    public Optional<UserInfo> getUserById(Long userId) {
        return repository.findById(userId);
    }
    public void deleteUser(Long userId) {
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
    public UserInfo updatePassword(Long userId, String currentPassword, String newPassword) {
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
    public void testerFindByEmail() {
        String usernameToTest = "test@gmail.com";
        Optional<UserInfo> userInfoOptional = repository.findByEmail(usernameToTest);

        if (userInfoOptional.isPresent()) {
            // L'utilisateur a été trouvé
            UserInfo userInfo = userInfoOptional.get();
            System.out.println("Utilisateur trouvé : " + userInfo.getName());
            System.out.println("Rôles de l'utilisateur : " + userInfo.getRoles());
        } else {
            // L'utilisateur n'a pas été trouvé
            System.out.println("Utilisateur non trouvé pour l'email : " + usernameToTest);
        }
    }
    public void deteletUserInfoByLivreurId(Long livreurId){
       UserInfo userInfo = repository.findByLivreurId(livreurId);
       repository.deleteById(userInfo.getId());
    }
    //statistic
    public Long getTotalUsers() {
        return repository.count();
    }
}
