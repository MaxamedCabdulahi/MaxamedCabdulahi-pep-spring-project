package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Check if a username already exists
    public boolean usernameExists(String username) {
        return accountRepository.existsByUsername(username);
    }

    // Create a new account
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Authenticate user using username and password
    public Account authenticate(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    // Check if an account exists based on account ID
    public boolean accountExists(int accountId) {
        return accountRepository.existsById(accountId);
    }
}
