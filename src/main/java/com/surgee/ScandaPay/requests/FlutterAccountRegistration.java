package com.surgee.ScandaPay.requests;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class FlutterAccountRegistration {
    private String email;
    private Boolean is_permanent;
    private String bvn;
    private String phonenumber;
    private String firstname;
    private String lastname;
    private String tx_ref;
    private String narration;
}
