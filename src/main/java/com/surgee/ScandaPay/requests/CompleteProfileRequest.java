package com.surgee.ScandaPay.requests;

import com.surgee.ScandaPay.model.Gender;

import org.springframework.web.multipart.MultipartFile;


import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteProfileRequest {
    private String phoneNumber;
    private String address;
    private String transactionPin;
    private MultipartFile profileImage;
    private String bvn;

    @Enumerated(value = EnumType.ORDINAL)
    private Gender gender;

}
