package com.bankProject.tekanaeWallet.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
    @GetMapping
    public String homePage() {
        return "Welcome to Tekana-eWallet app";
    }
}
