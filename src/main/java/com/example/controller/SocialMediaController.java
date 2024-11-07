package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



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

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return new ResponseEntity<>("Username cannot be blank", HttpStatus.BAD_REQUEST);
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return new ResponseEntity<>("Password must be at least 4 characters long", HttpStatus.BAD_REQUEST);
        }

        if (accountService.usernameExists(account.getUsername())) {
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }

        Account newAccount = accountService.createAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Account existingAccount = accountService.authenticate(account.getUsername(), account.getPassword());
        if (existingAccount != null) {
            return new ResponseEntity<>(existingAccount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    // Create a new message
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            return new ResponseEntity<>("Message text cannot be blank", HttpStatus.BAD_REQUEST);
        }
        if (message.getMessageText().length() > 255) {
            return new ResponseEntity<>("Message text exceeds 255 characters", HttpStatus.BAD_REQUEST);
        }
        if (!accountService.accountExists(message.getPostedBy())) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
    
        Message newMessage = messageService.createMessage(message);
        return new ResponseEntity<>(newMessage, HttpStatus.OK);  // Ensure message is returned with correct ID
    }    
    
    // Get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Get a message by its ID
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<?> getMessageById(@PathVariable int message_id) {
        Message message = messageService.getMessageById(message_id);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("", HttpStatus.OK); // Return empty response body when message not found
        }
    }

    // Delete a message by its ID
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int message_id) {
        // Try to delete the message and get the number of rows affected
        int rowsModified = messageService.deleteMessage(message_id);

        if (rowsModified == 0) {
            return new ResponseEntity<>(HttpStatus.OK); // Message not found, return 200 OK with empty body
        }

        // If the message was deleted, return 1 (row modified)
        return new ResponseEntity<>(rowsModified, HttpStatus.OK);
    }

    // Update a message by its ID
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessage(@PathVariable int message_id, @RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return new ResponseEntity<>("Invalid message text", HttpStatus.BAD_REQUEST);
        }

        // Try to update the message
        boolean isUpdated = messageService.updateMessage(message_id, message.getMessageText());
        if (!isUpdated) {
            return new ResponseEntity<>("Message not found", HttpStatus.BAD_REQUEST);  // Return 400 if the message doesn't exist
        }
             
        return new ResponseEntity<>(1, HttpStatus.OK);  // Return 1 (one row modified) if the update was successful
    }

    // Get all messages from a specific account
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int account_id) {
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}