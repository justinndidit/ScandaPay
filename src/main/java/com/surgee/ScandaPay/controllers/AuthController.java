package com.surgee.ScandaPay.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

import com.surgee.ScandaPay.DTO.HttpResponseDTO;

import com.surgee.ScandaPay.model.User;
import com.surgee.ScandaPay.requests.HttpLoginRequest;
import com.surgee.ScandaPay.requests.PasswordResetRequest;
import com.surgee.ScandaPay.services.AuthService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<HttpResponseDTO> registerUser(@RequestBody User user) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        boolean success = false;
    
        try {
            if(user.getLastName() == null || user.getFirstName() == null) {
                status = HttpStatus.BAD_REQUEST;
                throw new IllegalArgumentException("First name and last name are required");
            }
            
            return authService.registerUser(user);
        } catch (Error err) {
            return new ResponseEntity<>(HttpResponseDTO.builder()
                .data(null)
                .error(err.getMessage())
                .message("Login failed")
                .success(success)
                .status(status)
                .build(), status);
        }
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<HttpResponseDTO> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return authService.confirmEmail(confirmationToken);
    }

    @GetMapping("/confirm-account")
    public String confirmUserAccount() {
        return "User Email confirmed.";
    }

   

    // @GetMapping("/reset-password")
    // public String confirmUserAccountGet(@RequestParam("token")String confirmationToken) {
    //     return "Reset password";
    // }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request, @RequestParam("token")String confirmationToken) {
        
        return authService.resetPassword(request.getOldPassword(), request.getNewPassword(), confirmationToken);
        
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<HttpResponseDTO> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");   

        return authService.resetPasswordRequest(email);
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponseDTO> loginUser(@RequestBody HttpLoginRequest request) {
        System.out.println("request");
        System.out.println(request);

        System.out.println("Object");
    


        return authService.loginUser(request.getEmail(), request.getPassword());
    }
}

