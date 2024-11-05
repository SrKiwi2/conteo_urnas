package com.urnas.conteo;

import org.springframework.boot.SpringApplication;

import com.usic.conteo.ConteoApplication;

public class TestConteoApplication {

	public static void main(String[] args) {
		SpringApplication.from(ConteoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
