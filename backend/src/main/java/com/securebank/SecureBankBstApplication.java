package com.securebank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SecureBank BST Fraud Detection API.
 * Manages flagged transactions using a Binary Search Tree keyed by transaction ID.
 */
@SpringBootApplication
public class SecureBankBstApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureBankBstApplication.class, args);
    }
}
