package com.surgee.ScandaPay.DTO;

import com.surgee.ScandaPay.model.Role;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRegistrationDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private String accountNumber;
    private String accountBalance;
    private Integer tier;
    private Role role;
    private String tokenType;
    private String password;
    private String phoneNumber;
}
