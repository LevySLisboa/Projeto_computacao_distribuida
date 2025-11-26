package com.example.GestaoTurmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GestaoTurmasApplication {
	public static void main(String[] args) {
		SpringApplication.run(GestaoTurmasApplication.class, args);
	}

}
