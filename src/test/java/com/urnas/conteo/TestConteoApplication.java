package com.urnas.conteo;

import org.springframework.boot.SpringApplication;

public class TestConteoApplication {

	public static void main(String[] args) {
		SpringApplication.from(ConteoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
