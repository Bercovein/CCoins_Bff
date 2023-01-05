package com.ccoins.bff;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BffApplication
		extends SpringBootServletInitializer
{

	public static void main(String[] args) {

		SpringApplicationBuilder builder = new SpringApplicationBuilder(BffApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BffApplication.class);
	}

}
