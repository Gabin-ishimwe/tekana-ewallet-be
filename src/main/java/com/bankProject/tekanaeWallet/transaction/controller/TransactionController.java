package com.bankProject.tekanaeWallet.transaction.controller;

import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.transaction.dto.TransactionResponseDto;
import com.bankProject.tekanaeWallet.transaction.entity.Transaction;
import com.bankProject.tekanaeWallet.transaction.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@Api(tags = "Transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(
            value = "User's transaction",
            notes = "API to get all transactions of user"
    )
    public TransactionResponseDto getAllTransactions() throws NotFoundException {
        return transactionService.getAllUserTransaction();
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(
            value = "User's transaction",
            notes = "API to get one transaction by Id of user"
    )
    public TransactionResponseDto getOneTransactions(@PathVariable Long transactionId) throws NotFoundException {
        return transactionService.getOneUserTransaction(transactionId);
    }
}
