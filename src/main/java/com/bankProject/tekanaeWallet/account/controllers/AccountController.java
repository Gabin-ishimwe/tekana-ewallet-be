package com.bankProject.tekanaeWallet.account.controllers;

import com.bankProject.tekanaeWallet.account.dto.AccountRequestDto;
import com.bankProject.tekanaeWallet.account.dto.AccountResponseDto;
import com.bankProject.tekanaeWallet.account.entity.Account;
import com.bankProject.tekanaeWallet.account.services.AccountService;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@Api(tags = "Account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(
            value = "User deposit money",
            notes = "API for user to deposit money on their account"
    )
    public AccountResponseDto depositMoney(@RequestBody @Valid AccountRequestDto accountRequestDto) throws NotFoundException {
        return accountService.depositMoney(accountRequestDto);
    }
}
