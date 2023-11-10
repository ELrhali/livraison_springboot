package com.livraison.livreurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LivreursApplication {

    public static void main(String[] args) {
        SpringApplication.run(LivreursApplication.class, args);
    }

}
