package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ServiceLiveCodingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceLiveCodingApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/startDownload").allowedOrigins("http://localhost:3000");
				registry.addMapping("/genTree").allowedOrigins("http://localhost:3000");
			}
		};
		
	}
}
