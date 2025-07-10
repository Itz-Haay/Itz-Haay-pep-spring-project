package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null ||
            message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255 ||
            !accountRepository.existsById(message.getPostedBy())) {
                return null;            
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message deleteMessageById (int id) {
        Optional<Message> msg = messageRepository.findById(id);
        if (msg.isPresent()) {
            messageRepository.deleteById(id);
            return msg.get();
        }
        return null;
    }

    public Message updateMessage(int id, String text) {
        if (text == null || text.isBlank() || text.length() > 255) {
            return null;            
        }
        Optional<Message> existingMessage = messageRepository.findById(id);
        if (existingMessage.isPresent()) {
            Message message = existingMessage.get();
            message.setMessageText(text);
            return messageRepository.save(message);            
        }
        return null;
    }
    
    public List<Message> getMessagesByUserId (int id){
        return messageRepository.findByPostedBy(id);
    }
}
