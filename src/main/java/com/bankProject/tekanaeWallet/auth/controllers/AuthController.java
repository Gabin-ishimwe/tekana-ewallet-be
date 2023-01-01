package com.bankProject.tekanaeWallet.auth.controllers;

import com.bankProject.tekanaeWallet.auth.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.auth.dto.LoginDto;
import com.bankProject.tekanaeWallet.auth.dto.RegisterDto;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.exceptions.UserAuthException;
import com.bankProject.tekanaeWallet.exceptions.UserExistsException;
import com.bankProject.tekanaeWallet.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public AuthResponseDto authRegister(@RequestBody @Valid RegisterDto registerDto) throws UserExistsException, NotFoundException {
        return userService.userRegister(registerDto);
    }

    @PostMapping(path = "/login")
    public AuthResponseDto authLogin(@RequestBody @Valid LoginDto loginDto) throws NotFoundException, UserAuthException {
        return userService.userLogin(loginDto);
    }
}
