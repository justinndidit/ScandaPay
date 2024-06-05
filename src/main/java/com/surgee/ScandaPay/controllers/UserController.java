package com.surgee.ScandaPay.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surgee.ScandaPay.requests.CompleteProfileRequest;
import com.surgee.ScandaPay.services.UserService;
import com.surgee.ScandaPay.DTO.HttpResponseDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    
    @PutMapping("/profile/{user_id}")
    @Transactional
    public ResponseEntity<HttpResponseDTO> completeUserProfile(
        @PathVariable Long user_id,
        @ModelAttribute CompleteProfileRequest request){
            return userService.completeUserProfile(user_id, request.getPhoneNumber(),
            request.getAddress(),request.getTransactionPin(),request.getGender(), request.getProfileImage(), request.getBvn());
           
    }

    @GetMapping("/{user_id}/dashboard")
    public ResponseEntity<HttpResponseDTO> home(@PathVariable Long user_id) {
        return userService.dashboard(user_id);
    }


}
