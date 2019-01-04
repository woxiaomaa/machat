package com.ma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.ma.mapper")
public class MachatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachatApplication.class, args);
	}

}

