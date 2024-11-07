package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Create a new message 
    public Message createMessage(Message message) {
        return messageRepository.save(message);  // JPA will handle messageId assignment automatically
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Get a message by its ID
    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    // Delete a message by its ID
    public int deleteMessage(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);  // Delete the message
            return 1;  // Return 1 because one row was deleted
        }
        return 0;  // Return 0 if the message does not exist
    }
    
    public boolean updateMessage(int messageId, String newMessageText) {
        // Check if the message exists
        if (messageRepository.existsById(messageId)) {
            // Retrieve and update the message
            Message message = messageRepository.findById(messageId).get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return true;  // Return true if the update was successful
        }
        return false;  // Return false if the message does not exist
    }
    
    // Get all messages by a specific account
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
