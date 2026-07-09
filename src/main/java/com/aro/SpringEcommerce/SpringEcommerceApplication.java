package com.aro.SpringEcommerce;

import com.aro.SpringEcommerce.Storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class SpringEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEcommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StorageService storageService) {
		return args -> {
			System.out.println("Application started...");
			storageService.init();
		};
	}

}
