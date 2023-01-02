package com.bankProject.tekanaeWallet.account.services;

import com.bankProject.tekanaeWallet.account.dto.AccountRequestDto;
import com.bankProject.tekanaeWallet.account.dto.AccountResponseDto;
import com.bankProject.tekanaeWallet.account.entity.Account;
import com.bankProject.tekanaeWallet.account.repositories.AccountRepository;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountResponseDto depositMoney(AccountRequestDto accountRequestDto) throws NotFoundException {
        Optional<Account> findAccount = accountRepository.findById(accountRequestDto.getAccountNumber());
        if(findAccount.isEmpty()) {
            throw new NotFoundException("Account number not found");
        }
        Long newBalance = findAccount.get().getBalance() + accountRequestDto.getMoney();
        findAccount.get().setBalance(newBalance);
        Account depositAccount = accountRepository.save(findAccount.get());

        return new AccountResponseDto(
                "Amount deposited Successful",
                depositAccount
        );
    }
}
