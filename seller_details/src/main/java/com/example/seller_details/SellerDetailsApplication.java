package com.example.seller_details;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.example.seller_details", "com.seller_details"})
@EnableJpaRepositories(basePackages = {"com.seller_details.repositary", "com.example.seller_details.repositary"})
@EntityScan(basePackages = {"com.seller_details.model", "com.example.seller_details.model"})
public class SellerDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellerDetailsApplication.class, args);
	}

}
