package com.surgee.ScandaPay.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {
    public String generateTransferReference() {
        return UUID.randomUUID().toString().replace("-", ""); 
    }
}
