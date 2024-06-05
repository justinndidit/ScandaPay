package com.surgee.ScandaPay.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.surgee.ScandaPay.DAO.UserRepository;
import com.surgee.ScandaPay.DTO.HttpResponseDTO;
import com.surgee.ScandaPay.DTO.CompleteUserProfileDTO;
import com.surgee.ScandaPay.model.User;
import com.surgee.ScandaPay.util.CloudinaryFileUploader;
import com.surgee.ScandaPay.model.Gender;

@Service
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryFileUploader cloudinaryFileUploader;

    public ResponseEntity<HttpResponseDTO> completeUserProfile(Long id,String phoneNumber,
    String address, String transactionPin, Gender gender, MultipartFile profileImage, String bvn) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Boolean success = false;
        Integer statusCode = 5;
        try {
            if(id == null || phoneNumber == null || address == null) {
                throw new IllegalStateException("Invalid user ID");
            }

            if(transactionPin == null ) {
                status = HttpStatus.BAD_REQUEST;
                statusCode = 400;
                throw new IllegalStateException("Please enter Transaction pin.");

            }

            Optional<User> userOptional = userRepository.findById(id);

            if(!userOptional.isPresent()) {
                status = HttpStatus.NOT_FOUND;
                statusCode = 404;
                throw new IllegalStateException("User with ID not found.");
            }
            User user = userOptional.get();

            String profileImageUrl = cloudinaryFileUploader.uploadFile(profileImage);
            
            user.setPhoneNumber(phoneNumber);
            user.setBvn(bvn);
            user.setAddress(address);
            user.setGender(gender);;
            user.setTransactionPin(passwordEncoder.encode(transactionPin));
            user.setProfileImageUrl(profileImageUrl);

            userRepository.save(user);

            success = true;
            status = HttpStatus.OK;
            statusCode = 200;

            CompleteUserProfileDTO userData = CompleteUserProfileDTO.builder()
                                            .firstName(user.getFirstName())
                                            .lastName(user.getLastName())
                                            .email(user.getEmail())
                                            .phoneNumber(user.getPhoneNumber())
                                            .address(user.getAddress())
                                            .role(user.getRole())
                                            .gender(user.getGender())
                                            .accountBalance(user.getAccountBalance())
                                            .transactionPin(user.getTransactionPin())
                                            .accountNumber(user.getAccountNumber())
                                            .accountTier(user.getAccountTier())
                                            .bvn(user.getBvn())
                                            .profileImageUrl(profileImageUrl)
                                            .build();

            HttpResponseDTO response = HttpResponseDTO.builder()
                                        .data(userData)
                                        .error(null)
                                        .message("User profile updated successfully.")
                                        .success(success)
                                        .status(status)
                                        .statusCode(statusCode)
                                        .stackTrace(null)
                                        .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<> (HttpResponseDTO
            .builder()
            .data(null)
            .error(e.getMessage())
            .message(e.getMessage())
            .success(success)
            .status(status)
            .statusCode(statusCode)
            .stackTrace(e.getCause() != null ? e.getCause().toString() : null)
            .build(), status);
        }
    }

    public ResponseEntity<HttpResponseDTO> dashboard(Long user_id) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Integer statusCode = 500;
        Boolean success = false;
        try {
            Optional<User> userOptional = userRepository.findById(user_id);

            if(!userOptional.isPresent()) {
                statusCode = 404;
                status = HttpStatus.NOT_FOUND;
                throw new IllegalStateException("User with ID not found.");
            }

            User user = userOptional.get();
            status = HttpStatus.OK;
            statusCode = 200;
            success = true;

            CompleteUserProfileDTO userProfile = CompleteUserProfileDTO.builder()
                                                .firstName(user.getFirstName())
                                                .lastName(user.getLastName())
                                                .email(user.getEmail())
                                                .phoneNumber(user.getPhoneNumber())
                                                .transactionPin(user.getTransactionPin())
                                                .address(user.getAddress())
                                                .profileImageUrl(user.getProfileImageUrl())
                                                .accountTier(user.getAccountTier())
                                                .accountNumber(user.getAccountNumber())
                                                .accountBalance(user.getAccountBalance())
                                                .bvn(user.getBvn())
                                                .role(user.getRole())
                                                .gender(user.getGender())
                                                .build();
            
            HttpResponseDTO response = HttpResponseDTO.builder()
                                                .data(userProfile)
                                                .error(null)
                                                .message("Welcome to ScandaPay.")
                                                .success(success)
                                                .status(status)
                                                .statusCode(statusCode)
                                                .stackTrace(null)
                                                .build();
                    return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<> (HttpResponseDTO
            .builder()
            .data(null)
            .error(e.getMessage())
            .message(e.getMessage())
            .success(success)
            .status(status)
            .statusCode(statusCode)
            .stackTrace(e.getCause() != null ? e.getCause().toString() : null)
            .build(), status);
        }
 
    } 
}

