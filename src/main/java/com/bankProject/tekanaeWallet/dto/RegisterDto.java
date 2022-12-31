package com.bankProject.tekanaeWallet.dto;

import com.bankProject.tekanaeWallet.validation.passwordValidation.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotBlank(
            message = "First name is required"
    )
    private String firstName;

    @NotBlank(
            message = "Last name is required"
    )
    private String lastName;

    @NotBlank(
            message = "Email is required"
    )
    @Email(
            message = "Email must be valid"
    )
    private String email;

    @NotBlank(
            message = "PasswordAnnotation is required"
    )
    @Password
    private String password;

    @NotBlank(
            message = "Contact number is required"
    )
    private String ContactNumber;
}
