package com.example.service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





@Service
public class AccountService {
    private AccountRepository accountRepository;
    private MessageService messageService;

    @Autowired
    public AccountService(MessageService messageService, AccountRepository accountRepository){
        this.messageService = messageService;
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account newAccount){

        if(newAccount.getUsername().isEmpty() || newAccount.getPassword().length() < 4){
            return null;
        }
        accountRepository.save(newAccount);
        return newAccount;
    }

    public Account duplicateUsername(Account testAccount){
        return accountRepository.findByUsername(testAccount.getUsername());
    }

    public Account login(String username, String password){
        return accountRepository.findByUsernameAndPassword(username, password);
    }
}
