package com.example.messagingapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.messagingapp.model.Conversation;
import com.example.messagingapp.service.conversation.ConversationService;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/v1/conversations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("{userId}")
    public List<Conversation> findAllConversationByUserId(@PathVariable long userId) throws NotFoundException {
        log.info("Rest API to find conversations for userId {}", userId);
        return conversationService.findBySenderUserIdOrRecipientUserId(userId);
    }

    @GetMapping("{userName}")
    public List<Conversation> findAllConversationByUserName(@PathVariable String userName) throws NotFoundException {
        log.info("Rest API to find conversations for userName {}", userName);
        return conversationService.findBySenderUserNameOrRecipientUserName(userName);
    }
}
