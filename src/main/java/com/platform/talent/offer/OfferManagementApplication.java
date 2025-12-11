package com.platform.talent.offer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OfferManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfferManagementApplication.java, args);
    }
}

