package com.bankProject.tekanaeWallet.account.dto;

import com.bankProject.tekanaeWallet.validation.uuidValidation.Uuid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDto {

    @Uuid
    private UUID accountNumber;
    @Min(value = 0)
    private Long money;
}
