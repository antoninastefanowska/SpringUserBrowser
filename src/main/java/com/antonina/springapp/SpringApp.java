package com.antonina.springapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.antonina.springapp.repositories")
public class SpringApp {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringApp.class, args);
	}
}