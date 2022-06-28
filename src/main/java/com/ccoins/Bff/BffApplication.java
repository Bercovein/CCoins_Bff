package com.ccoins.Bff;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class BffApplication
//		extends SpringBootServletInitializer
{

	public static void main(String[] args) {

		SpringApplicationBuilder builder = new SpringApplicationBuilder(BffApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(BffApplication.class);
//	}

}
