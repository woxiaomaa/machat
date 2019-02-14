package com.ma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.ma.mapper")
@ComponentScan(basePackages= {"com.ma"})
public class MachatApplication {

	@Bean
	public SpringUtil getSpingUtil() {
		return new SpringUtil();
	}
	public static void main(String[] args) {
		SpringApplication.run(MachatApplication.class, args);
	}

}

