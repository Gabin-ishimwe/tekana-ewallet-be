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
        Optional<Account> findAccount = accountRepository.findById(accountRequestDto.getAccountNumber());
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findAccount.isEmpty()) {
            throw new NotFoundException("Account number not found");
        }
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        Long newBalance = findAccount.get().getBalance() + accountRequestDto.getMoney();
        findAccount.get().setBalance(newBalance);
        Account depositAccount = accountRepository.save(findAccount.get());

        Transaction deposit = new Transaction();
        deposit.setToAccountNumber(findAccount.get().getAccount_number());
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
        Optional<Account> findAccount = accountRepository.findById(accountRequestDto.getAccountNumber());
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findAccount.isEmpty()) {
            throw new NotFoundException("Account number not found");
        }
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        long remainingBalance = findAccount.get().getBalance() - accountRequestDto.getMoney();
        if(remainingBalance < 0) {
            throw new IllegalArgumentException("You don't have enough sufficient funds");
        }
        findAccount.get().setBalance(remainingBalance);
        Account withrawAccount = accountRepository.save(findAccount.get());

        Transaction withdraw = new Transaction();
        withdraw.setFromAccountNumber(findAccount.get().getAccount_number());
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
        Optional<Account> senderAccount = accountRepository.findById(transferRequestDto.getSenderAccount());
        Optional<Account> receiveAccount = accountRepository.findById(transferRequestDto.getReceiverAccount());
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        if(senderAccount.isEmpty()) {
            throw new NotFoundException("Sender account not found");
        }
        if(receiveAccount.isEmpty()) {
            throw new NotFoundException("Receiver account not found");
        }
        // sender's account
        // removing the amount
        long newBalance = senderAccount.get().getBalance() - transferRequestDto.getAmount();
        if(newBalance < 0) {
            throw new IllegalArgumentException("You don't have enough sufficient funds");
        }
        senderAccount.get().setBalance(newBalance);
        Account transferAccount = accountRepository.save(senderAccount.get());

        // receiver's account
        // adding the amount
        receiveAccount.get().setBalance(receiveAccount.get().getBalance() + transferRequestDto.getAmount());
        accountRepository.save(receiveAccount.get());

        // registering transaction
        Transaction transfer = new Transaction();
        transfer.setTypeTransaction(TypeTransaction.TRANSFER);
        transfer.setAmount(transferRequestDto.getAmount());
        transfer.setFromAccountNumber(senderAccount.get().getAccount_number());
        transfer.setToAccountNumber(receiveAccount.get().getAccount_number());

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
