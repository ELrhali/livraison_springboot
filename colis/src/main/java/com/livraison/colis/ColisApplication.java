package com.livraison.colis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ColisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColisApplication.class, args);
    }

}
