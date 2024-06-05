package com.surgee.ScandaPay.DTO;

import lombok.Data;
import lombok.Builder;

import com.surgee.ScandaPay.model.Role;
import com.surgee.ScandaPay.model.Gender;

@Data
@Builder
public class CompleteUserProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String transactionPin;
    private String profileImageUrl;
    private Integer accountTier;
    private String accountNumber;
    private String accountBalance;
    private Role role;
    private Gender gender;
    private String bvn;
}
