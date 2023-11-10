package com.livraison.livraisons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LivraisonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LivraisonsApplication.class, args);
    }

}
