package com.budgetbuddy.plaid.plaid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PlaidApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaidApplication.class, args);
	}

}
