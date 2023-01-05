package com.bankProject.tekanaeWallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@RestController
public class TekanaEWalletApplication {

	@GetMapping("/")
	public String welcomeApi() {
		return "Welcome to Tekana eWallet API, To access the API documentation navigate to /swagger-ui/index.html";
	}

	public static void main(String[] args) {
		SpringApplication.run(TekanaEWalletApplication.class, args);
	}

}
