package com.bankProject.tekanaeWallet.auth.controllers;

import com.bankProject.tekanaeWallet.auth.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.auth.dto.LoginDto;
import com.bankProject.tekanaeWallet.auth.dto.RegisterDto;
import com.bankProject.tekanaeWallet.auth.entity.User;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.exceptions.UserAuthException;
import com.bankProject.tekanaeWallet.exceptions.UserExistsException;
import com.bankProject.tekanaeWallet.auth.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Authentication")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    @ApiOperation(
            value = "User register",
            notes = "User registration in our application",
            response = AuthResponseDto.class
    )
    public AuthResponseDto authRegister(@RequestBody @Valid RegisterDto registerDto) throws UserExistsException, NotFoundException {
        return userService.userRegister(registerDto);
    }

    @PostMapping(path = "/login")
    @ApiOperation(
            value = "User login",
            notes = "User login authentication in our application"
    )
    public AuthResponseDto authLogin(@RequestBody @Valid LoginDto loginDto) throws NotFoundException, UserAuthException {
        return userService.userLogin(loginDto);
    }

    @GetMapping
    @ApiOperation(
            value = "Get all users",
            notes = "Api to get all application user"
    )
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
