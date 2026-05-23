package org.educastur.samuelepv59.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;

@SpringBootApplication
//@ComponentScan(basePackages = "org.educastur.samuelepv59.todo_list") // <--- FUERZA EL ESCANEO
public class TodoListApplication {

	@Autowired
	private Environment env;

	@PostConstruct
	public void init() {
		System.out.println(">>> ARCHIVO CARGADO: " + env.getProperty("spring.application.name"));
		System.out.println(">>> URL DATASOURCE: " + env.getProperty("spring.datasource.url"));
	}

	public static void main(String[] args) {

		SpringApplication.run(TodoListApplication.class, args);
	}
}