package com.ccoins.Bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BffApplication
//		extends SpringBootServletInitializer
{

	public static void main(String[] args) {
		SpringApplication.run(BffApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(BffApplication.class);
//	}

}
