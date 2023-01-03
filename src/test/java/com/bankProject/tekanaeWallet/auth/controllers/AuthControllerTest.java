package com.bankProject.tekanaeWallet.auth.controllers;

import com.bankProject.tekanaeWallet.AbstractTest;
import com.bankProject.tekanaeWallet.auth.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.auth.dto.RegisterDto;
import com.bankProject.tekanaeWallet.auth.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest extends AbstractTest {


    private int port = 9090;

    private static RestTemplate restTemplate;

    private String baseUrl = "http://localhost";


    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setup() {
       baseUrl =  baseUrl.concat(":").concat(port + "").concat("/api/v1/auth");
    }

    @Test
    void authRegister() {
        RegisterDto registerDto = RegisterDto.builder()
                .firstName("john")
                .lastName("doe")
                .email("john@gmail.com")
                .password("#Password123")
                .ContactNumber("078787576306")
                .build();

        AuthResponseDto response = restTemplate.postForObject(baseUrl + "/register", registerDto, AuthResponseDto.class);

        assert response != null;
        assertEquals("User Registered Successfully", response.getMessage());
    }

    @Test
    @Disabled
    void authLogin() {
    }

    @Test
    @Disabled
    void getAllUsers() {
    }

    @Test
    @Disabled
    void getOneUser() {
    }
}