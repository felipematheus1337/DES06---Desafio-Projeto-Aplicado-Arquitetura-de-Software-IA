package com.des06.ai_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiV1Application {

	public static void main(String[] args) {
		var envPath = java.nio.file.Path.of(".env").toAbsolutePath().normalize();

		System.out.println("Procurando .env em: " + envPath);
		System.out.println("Arquivo encontrado: " + java.nio.file.Files.isRegularFile(envPath));
		SpringApplication.run(AiV1Application.class, args);
	}

}
