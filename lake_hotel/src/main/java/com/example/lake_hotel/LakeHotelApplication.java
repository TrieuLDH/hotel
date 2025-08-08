package com.example.lake_hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class LakeHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(LakeHotelApplication.class, args);
    }

}
