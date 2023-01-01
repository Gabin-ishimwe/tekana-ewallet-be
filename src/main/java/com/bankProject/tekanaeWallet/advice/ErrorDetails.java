package com.bankProject.tekanaeWallet.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private String message;
    private Map<String, String> details;
    private HttpStatus httpStatus;
    private LocalDateTime timeStamp;
}
