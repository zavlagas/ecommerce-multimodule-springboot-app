package com.agileactors.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {


    @GetMapping
    public ResponseEntity<String> customers() {
        return ResponseEntity.ok("Hello from customer controller");
    }
}
