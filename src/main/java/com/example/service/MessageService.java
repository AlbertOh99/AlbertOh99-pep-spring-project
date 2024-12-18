package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages(){return messageRepository.findAll();}

    public Message getMessageByMessageId(int messageId){
        Message temp = messageRepository.findById(messageId).orElse(null);

        return temp;
    }

    public List<Message> getAllMessageByAccountId(int accountId){
        List<Message> messageByAccount = messageRepository.findByPostedBy(accountId);
        
        return messageByAccount;
    }

    public boolean deleteMessage(int messageId){
        Optional<Message> message = messageRepository.findById(messageId);
        
        if(message.isEmpty()){
            return false;
        }

        messageRepository.deleteById(messageId);
        return true;
    }

    public Message createNewMessage(Message message){
        if(message.getMessageText().length() > 255 || message.getMessageText().isEmpty() || accountRepository.findById(message.getPostedBy()).isEmpty()){
            return null;
        }
        messageRepository.save(message);

        return message;
    }

    public boolean updateMessage(int message, Message updateMessage){
        Message output = messageRepository.findById(message).orElse(null);
        if(output == null || updateMessage.getMessageText().isBlank() || updateMessage.getMessageText().length() > 255){
            return false;
        }
        messageRepository.save(updateMessage);
        return true;
    }
}
