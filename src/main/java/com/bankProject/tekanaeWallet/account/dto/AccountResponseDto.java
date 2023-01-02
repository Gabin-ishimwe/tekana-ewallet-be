package com.bankProject.tekanaeWallet.account.dto;

import com.bankProject.tekanaeWallet.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponseDto {
    private String message;
    private Account account;
}
