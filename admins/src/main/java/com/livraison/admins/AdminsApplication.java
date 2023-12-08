package com.livraison.admins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AdminsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminsApplication.class, args);
    }

}
