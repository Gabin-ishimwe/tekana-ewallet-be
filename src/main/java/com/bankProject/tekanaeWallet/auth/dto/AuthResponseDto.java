package com.bankProject.tekanaeWallet.auth.dto;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@Schema(description = "Auth Dto schema")
public class AuthResponseDto {
//    @Schema(description = "Response message")
    private String message;
    
//    @Schema(description = "Response token")
    private String token;
}
