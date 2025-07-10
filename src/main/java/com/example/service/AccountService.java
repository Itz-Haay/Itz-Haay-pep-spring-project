package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        if ((account.getUsername() == null) || (account.getPassword().length() < 4)) {
            return null;
        }

        Optional<Account> existingUser = accountRepository.findByUsername(account.getUsername());
        if (existingUser.isPresent()) {
            Account dummyAccount = new Account();
            dummyAccount.setAccountId(0);
            return dummyAccount;
        }
        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password).orElse(null);
    }
}
