package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;

import com.example.service.AccountService;
import com.example.service.MessageService;


import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.persistence.PostUpdate;
import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        Account test = accountService.duplicateUsername(account);
        if(test != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(test);
        }
        Account newAccount = accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.OK)
        .body(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        Account existingAccount = accountService.login(account.getUsername(), account.getPassword());
        if(existingAccount == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(existingAccount);
        }

        return ResponseEntity.status(HttpStatus.OK)
        .body(existingAccount);
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message newMessage = messageService.createNewMessage(message);
        if(newMessage == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(newMessage);
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> allMessages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK)
        .body(allMessages);
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId){
        Message message = messageService.getMessageByMessageId(messageId);

        return ResponseEntity.status(HttpStatus.OK)
        .body(message);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int messageId){

        boolean output = messageService.deleteMessage(messageId);
        if(output){
            return ResponseEntity.status(HttpStatus.OK)
            .body(1);
        }
        return ResponseEntity.status(HttpStatus.OK)
        .body(null);
    }
    
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message messageUpdate){
        boolean output = messageService.updateMessage(messageId, messageUpdate);
        if(output){
            return ResponseEntity.status(HttpStatus.OK)
            .body(1);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(null);        
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllAccountMessage(@PathVariable int accountId){
        List<Message> allMessages = messageService.getAllMessageByAccountId(accountId);

        return ResponseEntity.status(HttpStatus.OK)
        .body(allMessages);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handledUnauthorized(AuthenticationException e){return e.getMessage();}




}

