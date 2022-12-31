package com.bankProject.tekanaeWallet.advice;

import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.exceptions.UserAuthException;
import com.bankProject.tekanaeWallet.exceptions.UserExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidArgument(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errorMessages = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(
                fieldError -> {
                    errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
        );
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                "Failed validation",
                errorMessages,
                HttpStatus.BAD_REQUEST,
                errorTime
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<?> handleUserFoundException(UserExistsException userExistsException) {
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                userExistsException.getMessage(),
                null,
                HttpStatus.BAD_REQUEST,
                errorTime
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception) {
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                exception.getMessage(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR,
                errorTime
        ), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException notFoundException) {
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                notFoundException.getMessage(),
                null,
                HttpStatus.NOT_FOUND,
                errorTime
        ), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<?> handleInvalidCredentialException(UserAuthException userAuthException) {
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                userAuthException.getMessage(),
                null,
                HttpStatus.UNAUTHORIZED,
                errorTime
        ), HttpStatus.UNAUTHORIZED
        );
    }
}
