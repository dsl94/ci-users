package com.ciusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class CiUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CiUsersApplication.class, args);
	}
}
