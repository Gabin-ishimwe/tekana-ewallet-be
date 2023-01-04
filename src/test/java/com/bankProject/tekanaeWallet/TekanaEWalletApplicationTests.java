package com.bankProject.tekanaeWallet;

import com.bankProject.tekanaeWallet.account.dto.AccountRequestDto;
import com.bankProject.tekanaeWallet.account.dto.AccountResponseDto;
import com.bankProject.tekanaeWallet.account.dto.TransferRequestDto;
import com.bankProject.tekanaeWallet.auth.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.auth.dto.LoginDto;
import com.bankProject.tekanaeWallet.auth.dto.RegisterDto;
import com.bankProject.tekanaeWallet.auth.entity.User;
import com.bankProject.tekanaeWallet.auth.repositories.UserRepository;
import com.bankProject.tekanaeWallet.transaction.dto.TransactionResponseDto;
import com.bankProject.tekanaeWallet.utils.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TekanaEWalletApplicationTests extends AbstractTest {

	@LocalServerPort
	private int port;

	private static RestTemplate restTemplate;

	@Value("${app.baseUrl}")
	private String baseUrl;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {
		baseUrl =  baseUrl.concat(":").concat(port + "").concat("/api/v1");
	}

	@Test
	@DisplayName("Test API to register user")
	void authRegister() {
		RegisterDto registerDto = RegisterDto.builder()
				.firstName("john")
				.lastName("doe")
				.email("marry@gmail.com")
				.password("#Password123")
				.ContactNumber("078787576306")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/register", registerDto, AuthResponseDto.class);

		assert response != null;
		assertEquals("User Registered Successfully", response.getMessage());
	}

	@Test
	@DisplayName("Test API to login user")
	void authLogin() {
		// testing on seed data
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);

		assert response != null;
		assertEquals(response.getMessage(), "User Logged in Successfully");
	}

	@Test
	@DisplayName("Test API to get all users")
	void getAllUsers() {
		// authenticate user
		LoginDto loginDto = LoginDto.builder()
				.email("angel@gmail.com")
				.password("#Password567")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);
		assert response != null;
		String token = response.getToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		// testing on seed users
		ResponseEntity<List> responseUsers = restTemplate.exchange(baseUrl + "/auth/users", HttpMethod.GET, httpEntity, List.class);

		assertFalse(Objects.requireNonNull(responseUsers.getBody()).isEmpty());
	}

	@Test
	@DisplayName("Test API to get one user")
	void getOneUser() {
		// authenticate user
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);
		assert response != null;
		String token = response.getToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<User> userProfile = restTemplate.exchange(baseUrl + "/auth/profile", HttpMethod.GET, httpEntity, User.class);

		assertEquals("john@gmail.com", Objects.requireNonNull(userProfile.getBody()).getEmail());
	}

	@Test
	@DisplayName("Test API to deposit money on account")
	void depositMoney() {
		// authenticate user
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);
		assert response != null;
		String token = response.getToken();

		// create request
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		String email = jwtUtil.getUserNameFromToken(token);
		User findUser = userRepository.findByEmail(email);
		AccountRequestDto accountRequestDto = AccountRequestDto.builder()
				.accountNumber(findUser.getAccount().getAccount_number())
				.money(5000L)
				.build();

		HttpEntity<AccountRequestDto> httpEntity = new HttpEntity<AccountRequestDto>(accountRequestDto, headers);
		ResponseEntity<AccountResponseDto> depositResponse = restTemplate.exchange(baseUrl + "/account/deposit", HttpMethod.POST, httpEntity, AccountResponseDto.class);

		assertEquals(Objects.requireNonNull(depositResponse.getBody()).getMessage(), "Amount deposited Successful");
	}

	@Test
	@DisplayName("Test API to withdraw money on account")
	void withdrawMoney() {
		// authenticate user
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);
		assert response != null;
		String token = response.getToken();

		// create request
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		String email = jwtUtil.getUserNameFromToken(token);
		User findUser = userRepository.findByEmail(email);
		AccountRequestDto accountRequestDto = AccountRequestDto.builder()
				.accountNumber(findUser.getAccount().getAccount_number())
				.money(1000L)
				.build();

		HttpEntity<AccountRequestDto> httpEntity = new HttpEntity<AccountRequestDto>(accountRequestDto, headers);
		ResponseEntity<AccountResponseDto> depositResponse = restTemplate.exchange(baseUrl + "/account/withdraw", HttpMethod.POST, httpEntity, AccountResponseDto.class);

		assertEquals(Objects.requireNonNull(depositResponse.getBody()).getMessage(), "Amount withdrawn Successful");
	}

	@Test
	@DisplayName("Test API to transfer money to other accounts")
	void transferMoney() {
		// authenticate user
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);
		assert response != null;
		String token = response.getToken();

		// create request
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		String email = jwtUtil.getUserNameFromToken(token);
		User findUser = userRepository.findByEmail(email);
		User receiver = userRepository.findByEmail("angel@gmail.com");
		TransferRequestDto transferRequestDto = TransferRequestDto.builder()
				.senderAccount(findUser.getAccount().getAccount_number())
				.receiverAccount(receiver.getAccount().getAccount_number())
				.amount(2000L)
				.build();

		HttpEntity<TransferRequestDto> httpEntity = new HttpEntity<TransferRequestDto>(transferRequestDto, headers);
		ResponseEntity<AccountResponseDto> depositResponse = restTemplate.exchange(baseUrl + "/account/transfer", HttpMethod.POST, httpEntity, AccountResponseDto.class);

		assertEquals(Objects.requireNonNull(depositResponse.getBody()).getMessage(), "Amount Transferred Successfully");
	}

	@Test
	@DisplayName("Test API to get all users transactions")
	void getAllTransactions() {
		// authenticate user
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);
		assert response != null;
		String token = response.getToken();

		// create request
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		String email = jwtUtil.getUserNameFromToken(token);
		User findUser = userRepository.findByEmail(email);

		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		ResponseEntity<TransactionResponseDto> depositResponse = restTemplate.exchange(baseUrl + "/transaction", HttpMethod.GET, httpEntity, TransactionResponseDto.class);

		assertEquals(Objects.requireNonNull(depositResponse.getBody()).getMessage(), "User's Transactions");
	}

	@Test
	@DisplayName("Test API to get one user transaction")
	void getOneTransaction() {
		// authenticate user
		LoginDto loginDto = LoginDto.builder()
				.email("john@gmail.com")
				.password("#Password123")
				.build();

		AuthResponseDto response = restTemplate.postForObject(baseUrl + "/auth/login", loginDto, AuthResponseDto.class);
		assert response != null;
		String token = response.getToken();

		// create request
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		String email = jwtUtil.getUserNameFromToken(token);
		User findUser = userRepository.findByEmail(email);
		long accountNum = findUser.getTransactions().get(0).getId();

		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		ResponseEntity<TransactionResponseDto> depositResponse = restTemplate.exchange(baseUrl + "/transaction/" + accountNum, HttpMethod.GET, httpEntity, TransactionResponseDto.class);
		assertFalse(Objects.requireNonNull(depositResponse.getBody()).getTransactions().isEmpty());
	}

}
