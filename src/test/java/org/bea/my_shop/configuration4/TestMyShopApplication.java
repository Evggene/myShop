package org.bea.my_shop.configuration4;

import org.bea.my_shop.MyShopApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class TestMyShopApplication {

	public static void main(String[] args) {
		SpringApplication.from(MyShopApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
