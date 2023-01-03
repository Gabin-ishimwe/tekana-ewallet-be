package com.bankProject.tekanaeWallet.transaction.service;

import com.bankProject.tekanaeWallet.auth.entity.User;
import com.bankProject.tekanaeWallet.auth.repositories.UserRepository;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.transaction.entity.Transaction;
import com.bankProject.tekanaeWallet.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Transaction> getAllUserTransaction() throws NotFoundException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        return findUser.getTransactions();
    }

    public List<Transaction> getOneUserTransaction(Long transactionId) throws NotFoundException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        return findUser.getTransactions().stream().filter(transaction -> Objects.equals(transaction.getId(), transactionId)).toList();
    }
}
