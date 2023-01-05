package com.bankProject.tekanaeWallet.account.services;

import com.bankProject.tekanaeWallet.account.dto.AccountRequestDto;
import com.bankProject.tekanaeWallet.account.dto.AccountResponseDto;
import com.bankProject.tekanaeWallet.account.dto.TransferRequestDto;
import com.bankProject.tekanaeWallet.account.entity.Account;
import com.bankProject.tekanaeWallet.account.repositories.AccountRepository;
import com.bankProject.tekanaeWallet.auth.entity.User;
import com.bankProject.tekanaeWallet.auth.repositories.UserRepository;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.transaction.entity.Transaction;
import com.bankProject.tekanaeWallet.transaction.entity.TypeTransaction;
import com.bankProject.tekanaeWallet.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AccountResponseDto depositMoney(AccountRequestDto accountRequestDto) throws NotFoundException {
        Account findAccount = accountRepository.findById(accountRequestDto.getAccountNumber()).orElseThrow(() -> new NotFoundException("Account number not found"));
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        Long newBalance = findAccount.getBalance() + accountRequestDto.getMoney();
        findAccount.setBalance(newBalance);
        Account depositAccount = accountRepository.save(findAccount);

        Transaction deposit = new Transaction();
        deposit.setToAccountNumber(findAccount.getAccount_number());
        deposit.setAmount(accountRequestDto.getMoney());
        deposit.setTypeTransaction(TypeTransaction.DEPOSIT);

        Transaction depositTransaction = transactionRepository.save(deposit);
        findUser.getTransactions().add(depositTransaction);
        userRepository.save(findUser);

        return new AccountResponseDto(
                "Amount deposited Successful",
                depositAccount
        );
    }

    @Transactional
    public AccountResponseDto withdrawMoney(AccountRequestDto accountRequestDto) throws NotFoundException {
        Account findAccount = accountRepository.findById(accountRequestDto.getAccountNumber()).orElseThrow(() -> new NotFoundException("Account number not found"));
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        long remainingBalance = findAccount.getBalance() - accountRequestDto.getMoney();
        if(remainingBalance < 0) {
            throw new IllegalArgumentException("You don't have enough sufficient funds");
        }
        findAccount.setBalance(remainingBalance);
        Account withrawAccount = accountRepository.save(findAccount);

        Transaction withdraw = new Transaction();
        withdraw.setFromAccountNumber(findAccount.getAccount_number());
        withdraw.setAmount(accountRequestDto.getMoney());
        withdraw.setTypeTransaction(TypeTransaction.WITHDRAW);

        Transaction withdrawTransaction = transactionRepository.save(withdraw);
        findUser.getTransactions().add(withdrawTransaction);
        userRepository.save(findUser);

        return new AccountResponseDto(
                "Amount withdrawn Successful",
                withrawAccount
        );
    }

    @Transactional
    public AccountResponseDto transferMoney(TransferRequestDto transferRequestDto) throws NotFoundException {
        Account senderAccount = accountRepository.findById(transferRequestDto.getSenderAccount()).orElseThrow(()-> new NotFoundException("Sender account not found"));
        Account receiveAccount = accountRepository.findById(transferRequestDto.getReceiverAccount()).orElseThrow(() -> new NotFoundException("Receiver account not found"));
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        // sender's account
        // removing the amount
        long newBalance = senderAccount.getBalance() - transferRequestDto.getAmount();
        if(newBalance < 0) {
            throw new IllegalArgumentException("You don't have enough sufficient funds");
        }
        senderAccount.setBalance(newBalance);
        Account transferAccount = accountRepository.save(senderAccount);

        // receiver's account
        // adding the amount
        receiveAccount.setBalance(receiveAccount.getBalance() + transferRequestDto.getAmount());
        accountRepository.save(receiveAccount);

        // registering transaction
        Transaction transfer = new Transaction();
        transfer.setTypeTransaction(TypeTransaction.TRANSFER);
        transfer.setAmount(transferRequestDto.getAmount());
        transfer.setFromAccountNumber(senderAccount.getAccount_number());
        transfer.setToAccountNumber(receiveAccount.getAccount_number());

        Transaction transferTransaction = transactionRepository.save(transfer);
        findUser.getTransactions().add(transferTransaction);
        userRepository.save(findUser);
        return new AccountResponseDto("Amount Transferred Successfully", transferAccount);
    }

    public AccountResponseDto balanceAccount() throws NotFoundException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        Account userAccount = findUser.getAccount();

        return new AccountResponseDto("Account Balance", userAccount);
    }
}
