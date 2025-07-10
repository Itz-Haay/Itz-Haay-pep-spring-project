package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // 1. Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        Account created = accountService.register(account);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        } else if (created.getAccountId() == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(created);
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        Account loggedIn = accountService.login(username, password);
        if (loggedIn == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(loggedIn);
    }

    // 3. Create a new message
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        Message created = messageService.createMessage(message);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(created);
    }

    // 4. Get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    // 5. Get a message by ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message); 
    }

    // 6. Delete a message
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable int messageId) {
        Message deleted = messageService.deleteMessageById(messageId);
        if (deleted == null) {
            return ResponseEntity.ok().build(); 
        }
        return ResponseEntity.ok(1);
    }

    // 7. Update message text
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Message newMessage) {
        String newText = newMessage.getMessageText();
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return ResponseEntity.badRequest().build();            
        }
        Message updatedMessage = messageService.updateMessage(messageId, newText);
        if (updatedMessage == null) {
            return ResponseEntity.badRequest().build();            
        }        
        return ResponseEntity.ok(1);
    }

    // 8. Get all messages from a specific user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable int accountId) {
        return ResponseEntity.ok(messageService.getMessagesByUserId(accountId));
    }
}

