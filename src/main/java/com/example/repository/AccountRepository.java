package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // Check if username already exists
    boolean existsByUsername(String username);

    // Find an account by username and password for authentication
    Account findByUsernameAndPassword(String username, String password);
}
