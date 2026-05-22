package org.educastur.samuelepv59.todo_list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.educastur.samuelepv59.todo_list") // <--- FUERZA EL ESCANEO
public class TodoListApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoListApplication.class, args);
	}
}