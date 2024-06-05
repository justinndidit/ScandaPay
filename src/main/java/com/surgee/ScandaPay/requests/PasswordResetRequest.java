package com.surgee.ScandaPay.requests;

import lombok.Data;

@Data
public class PasswordResetRequest {
    String oldPassword;
    String newPassword;
}
