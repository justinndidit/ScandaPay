package com.surgee.ScandaPay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.*;
import io.github.cdimascio.dotenv.Dotenv;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {
    private final Dotenv dotenv;
    
    @Bean
    public Cloudinary cloudinary() {
        String Cloudinary_API_SECRET = dotenv.get("Cloudinary_API_SECRET");
        String Cloudinary_API_KEY = dotenv.get("Cloudinary_API_KEY");
        String Cloudinary_Cloud_Name = dotenv.get("Cloudinary_Cloud_Name");
        String CLOUDINARY_URL=String.format("cloudinary://%s:%s@%s",Cloudinary_API_KEY,Cloudinary_API_SECRET,Cloudinary_Cloud_Name);
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        cloudinary.config.secure = true;

        return cloudinary;
    }
}
