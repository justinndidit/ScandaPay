package com.surgee.ScandaPay.services.Impl;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

     @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

}
