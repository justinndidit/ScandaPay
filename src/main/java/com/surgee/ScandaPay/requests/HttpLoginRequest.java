package com.surgee.ScandaPay.requests;

import lombok.Data;

@Data
public class HttpLoginRequest {
    private String email;
   private String password;
}
