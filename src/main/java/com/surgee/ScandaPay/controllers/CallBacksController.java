package com.surgee.ScandaPay.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/callbacks")
public class CallBacksController {

     @PostMapping("transfer")
    public ResponseEntity<?> transferCallback() {
        System.out.print("Call back hit");
        return null;
    }

}
