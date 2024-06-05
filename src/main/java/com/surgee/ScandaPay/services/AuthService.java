package com.surgee.ScandaPay.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.reactive.function.client.WebClient;


import com.surgee.ScandaPay.DAO.UserRepository;
import com.surgee.ScandaPay.DAO.ConfirmationTokenRepository;
import com.surgee.ScandaPay.DAO.FlutterUserDetailsRepository;
import com.surgee.ScandaPay.services.Impl.EmailService;
import com.surgee.ScandaPay.DTO.HttpResponseDTO;
import com.surgee.ScandaPay.DTO.UserRegistrationDTO;
import com.surgee.ScandaPay.model.ConfirmationToken;
import com.surgee.ScandaPay.model.User;
import com.surgee.ScandaPay.requests.FlutterAccountRegistration;
import com.surgee.ScandaPay.ThirdPartyResponseData.Flutter_OK;


// import com.surgee.ScandaPay.util.CloudinaryFileUploader;


import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Builder
public class AuthService {
    private final UserRepository userRepository;
    private final FlutterUserDetailsRepository flutterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final WebClient webClient;
    // private final CloudinaryFileUploader fileUpload;

    public ResponseEntity<HttpResponseDTO> registerUser(User user) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Integer statusCode = 500;
        Boolean success = false;
        try{
            if(user.getEmail() == null || user.getPassword() == null || user.getFirstName() == null || user.getLastName() == null) {
                status = HttpStatus.BAD_REQUEST;
                statusCode = 400;
                throw new IllegalStateException("All fields are required");
            }
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            
            if(existingUser.isPresent()) {
                status = HttpStatus.CONFLICT;
                statusCode = 409;
                throw new IllegalStateException("User already exists");
            }

            FlutterAccountRegistration flutterClient = FlutterAccountRegistration.builder()
                .email(user.getEmail())
                .is_permanent(true)
                .bvn("12345678901")                  
                .tx_ref("VA12")
                .phonenumber(null)
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .narration(user.getFirstName() + " " + user.getLastName())
                .build();

            Flutter_OK flutterResponse = webClient.post()
                    .uri("/virtual-account-numbers")
                    .bodyValue(flutterClient)
                    .retrieve()
                    .bodyToMono(Flutter_OK.class)
                    .block();
            if(!flutterResponse.getStatus().equals("success")) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                statusCode = 500;
                throw new IllegalStateException("Something went wrong.");
            }

            
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAccountNumber(flutterResponse.getData().getAccount_number());
            user.setAccountBalance(flutterResponse.getData().getAmount());
            user.setAccountTier(4);


            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8000/auth/confirm-account?token="+confirmationToken.getConfirmationToken());
            emailService.sendEmail(mailMessage);
    
            String token = jwtService.generateToken(user);
            userRepository.save(user);
            flutterResponse.getData().setUser(user);
            flutterRepository.save(flutterResponse.getData());


            status = HttpStatus.OK;
            statusCode = 200;
            success = true;
    
            UserRegistrationDTO data =  UserRegistrationDTO.builder()
                   .email(user.getEmail())
                   .firstName(user.getFirstName())
                   .lastName(user.getLastName())
                   .id(user.getId())
                   .token(token)
                   .accountNumber(user.getAccountNumber())
                   .accountBalance(user.getAccountBalance())
                   .phoneNumber(user.getPhoneNumber())
                    .tier(user.getAccountTier())
                   .role(user.getRole())
                   .tokenType("Bearer")
                   .password(user.getPassword())
                   .build();

            HttpResponseDTO response = HttpResponseDTO.builder()
                                   .data(data)
                                   .error(null)
                                   .message("Please Confirm Email to verify account")
                                   .success(success)
                                   .status(status)
                                   .statusCode(statusCode)
                                   .build();
            return new ResponseEntity<>(response, status);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpResponseDTO.builder()
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

    public ResponseEntity<HttpResponseDTO> loginUser(String email, String password) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Boolean success = false;
        Integer statusCode = 500;
        try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        Optional<User> user = userRepository.findByEmail(email);

        if(user.get() == null) {
            status = HttpStatus.NOT_FOUND;
            statusCode = 404;
            throw new IllegalStateException("User not found");
            
        }

        status = HttpStatus.OK;
        success = true;
        statusCode = 200;


        String token = jwtService.generateToken(user.get());
        UserRegistrationDTO userData =  UserRegistrationDTO.builder()
               .email(user.get().getEmail())
               .firstName(user.get().getFirstName())
               .lastName(user.get().getLastName())
               .id(user.get().getId())
               .role(user.get().getRole())
               .token(token)
               .password(user.get().getPassword())
               .tokenType("Bearer")
               .build();

        HttpResponseDTO response = HttpResponseDTO.builder()
                                    .data(userData)
                                    .error(null)
                                    .message("Login successful")
                                    .success(success)
                                    .status(status)
                                    .statusCode(statusCode)
                                    .build();
        return new ResponseEntity<>(response, status);  
       } catch (Error err) {
        return new ResponseEntity<>(HttpResponseDTO.builder()
                .data(null)
                .error(err.getMessage())
                .message(err.getMessage())
                .success(success)
                .status(status)
                .statusCode(statusCode)
                .stackTrace(null)
                .build(), status);
       }

    }

    public ResponseEntity<HttpResponseDTO> confirmEmail(String confirmationToken) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Boolean success = false;
        Integer statusCode = 500;
        try {
            if(confirmationToken == null) {
                status = HttpStatus.BAD_REQUEST;
                statusCode = 400;
                throw new IllegalArgumentException("Token is required");
            }
            Optional<ConfirmationToken> token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    
            if(!token.isPresent()) {
                status = HttpStatus.UNAUTHORIZED;
                statusCode = 401;
                throw new IllegalArgumentException("Token is invalid");
            }

            status = HttpStatus.OK;
            success = true;
            statusCode = 200;
            
            User user = userRepository.findByEmail(token.get().getUser().getEmail()).get();
            user.setIsEnabled(true);
            userRepository.save(user);
            HttpResponseDTO response = HttpResponseDTO.builder()
                    .data(user)
                    .error(null)
                    .message("Email verified successfully!")
                    .success(success)
                    .status(status)
                    .statusCode(statusCode)
                    .build();
            return new ResponseEntity<>(response, status);

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
    public ResponseEntity<HttpResponseDTO> resetPasswordRequest(String email) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Integer statusCode = 500;
        Boolean success = false;
    
        try {
            Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email.trim());

            if (!userOptional.isPresent()) {
                status = HttpStatus.NOT_FOUND;
                statusCode = 404;
                throw new IllegalArgumentException("User with email not found");
            }
    
            User user = userOptional.get();
            
           Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findByUserId(user.getId());

            if (!confirmationTokenOptional.isPresent()) {
                status = HttpStatus.NOT_FOUND;
                statusCode = 404;
                throw new IllegalArgumentException("User with email not found");
            }

            ConfirmationToken confirmationToken = confirmationTokenOptional.get();
    
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Password Reset Request!");
            mailMessage.setText("To reset your password, please click here : "
                    + "http://localhost:8000/auth/reset-password?token=" + confirmationToken.getConfirmationToken());
            emailService.sendEmail(mailMessage);
    
            success = true;
            status = HttpStatus.OK;
            statusCode = 200;
    
            HttpResponseDTO response = HttpResponseDTO.builder()
                    .data(user)
                    .error(null)
                    .message("Please Confirm Email to complete password reset request")
                    .success(success)
                    .status(status)
                    .statusCode(statusCode)
                    .build();
    
            return new ResponseEntity<>(response, status);
    
        }  catch (Exception e) {
            return new ResponseEntity<>(HttpResponseDTO
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
    
    public ResponseEntity<HttpResponseDTO> resetPassword(String oldPassword, String newPassword, String confirmationToken) {
        Boolean success = false;
        Integer statusCode = 500;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            if (confirmationToken == null) {
                statusCode = 400;
                status = HttpStatus.BAD_REQUEST;
                throw new IllegalArgumentException("Token is required");
            }

            if (oldPassword == null || newPassword == null) {
                statusCode = 400;
                status = HttpStatus.BAD_REQUEST;
                throw new IllegalArgumentException("Old password and new password are required");
            }
    
            if (oldPassword.equals(newPassword)) {
                statusCode = 400;
                status = HttpStatus.BAD_REQUEST;
                throw new IllegalArgumentException("New password cannot be the same as old password");
            }

            Optional<ConfirmationToken> token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
            if(!token.isPresent()) {
               statusCode = 401;
                status = HttpStatus.UNAUTHORIZED;
                throw new IllegalArgumentException("Password reset token is invalid");

            }

            User user = userRepository.findByEmail(token.get().getUser().getEmail()).get();
            if(!bCryptPasswordEncoder.matches(oldPassword,user.getPassword())) {
                statusCode = 401;
                status = HttpStatus.UNAUTHORIZED;
                throw new IllegalArgumentException("Old password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            statusCode = 200;
            success = true;
            status = HttpStatus.OK;

            HttpResponseDTO response = HttpResponseDTO.builder()
            .data(user)
            .error(null)
            .message("User password reset successfully")
            .success(success)
            .status(status)
            .statusCode(statusCode)
            .stackTrace(null)
            .build();

            return new ResponseEntity<>(response, status);  

        } catch(Exception e) {
            return new ResponseEntity<>(HttpResponseDTO.builder()
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
