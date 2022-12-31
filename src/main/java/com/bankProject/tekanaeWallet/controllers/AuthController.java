package com.bankProject.tekanaeWallet.controllers;

import com.bankProject.tekanaeWallet.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.dto.RegisterDto;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.exceptions.UserExistsException;
import com.bankProject.tekanaeWallet.services.UserService;
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
}
