package com.livraison.apigatewayservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ApigatewayservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayservicesApplication.class, args);
    }

}
