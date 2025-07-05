package org.bea.showcase;

import org.bea.showcase.application.configuration.RedisConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class ShowcaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowcaseApplication.class, args);
	}

}
