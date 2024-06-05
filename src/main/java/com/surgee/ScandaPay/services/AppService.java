package com.surgee.ScandaPay.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.surgee.ScandaPay.DTO.HttpResponseDTO;


@Service
public class AppService {

    public ResponseEntity<?> home() {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Integer statusCode = 500;
        Boolean success = false;

        try {

            status = HttpStatus.OK;
            statusCode = 200;
            success = true;
            

            return new ResponseEntity<> (HttpResponseDTO
            .builder()
            .data("Welcome to ScandaPay")
            .error(null)
            .message("Welcome to ScandaPay")
            .success(success)
            .status(status)
            .statusCode(statusCode)
            .build(), status);

            
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

}
