package com.example.dpe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DpeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpeApplication.class, args);
    }

}
