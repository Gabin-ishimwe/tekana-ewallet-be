package com.bankProject.tekanaeWallet.home.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Home")
public class HomeController {
    @GetMapping("/")
    @ApiOperation(
            value = "Home route",
            notes = "Entry point of the application"
    )
    public String welcomeApi() {
        return "Welcome to Tekana eWallet API, To access the API documentation navigate to /swagger-ui/index.html";
    }
}
