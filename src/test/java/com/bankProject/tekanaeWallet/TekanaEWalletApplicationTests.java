package com.bankProject.tekanaeWallet;

import com.bankProject.tekanaeWallet.auth.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.auth.dto.LoginDto;
import com.bankProject.tekanaeWallet.auth.dto.RegisterDto;
import com.bankProject.tekanaeWallet.auth.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TekanaEWalletApplicationTests extends AbstractTest {

	@LocalServerPort
	private int port;

	private static RestTemplate restTemplate;

	@Value("${app.baseUrl}")
	private String baseUrl;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {
		baseUrl =  baseUrl.concat(":").concat(port + "").concat("/api/v1/auth");
	}

	@Test
	@DisplayName("API to register user")
	void authRegister() {
		RegisterDto registerDto = RegisterDto.builder()
				.firstName("john")
				.lastName("doe")
				.email("marry@gmail.com")
				.password("#Password123")
				.ContactNumber("078787576306")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/register", registerDto, AuthResponseDto.class);

		assert response != null;
		assertEquals("User Registered Successfully", response.getMessage());
	}

	@Test
	@DisplayName("API to login user")
	void authLogin() {
		// testing on seed data
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/login", loginDto, AuthResponseDto.class);

		assert response != null;
		assertEquals(response.getMessage(), "User Logged in Successfully");
	}

	@Test
	@DisplayName("API to get all users")
	void getAllUsers() {
		// testing on seed users
		List response = restTemplate.getForObject(baseUrl + "/users", List.class);

		assert response != null;
		assertEquals(response.size(), 3);
	}

	@Test
	@DisplayName("API to get one user")
	void getOneUser() {

	}


}
