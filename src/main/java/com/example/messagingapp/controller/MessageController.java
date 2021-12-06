package com.example.messagingapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.messagingapp.exceptions.IsSameUserException;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.dto.MessageDto;
import com.example.messagingapp.service.message.MessageService;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("send-message")
    public Message sendMessage(@RequestBody MessageDto messageDto) throws NotFoundException, IsSameUserException {
        log.info("Rest API to send message {}", messageDto.toString());

        if (messageDto.getSenderNickName().equals(messageDto.getRecipientNickName())) {
            throw new IsSameUserException(
                    String.format("Sender %1$s and Recipient %2$s cannot be same.", messageDto.getSenderNickName(), messageDto.getRecipientNickName()));
        }
        return messageService.sendMessage(messageDto);
    }

    @GetMapping("/sender-name/{nickName}")
    public List<Message> findAllMessagesBySenderUserName(@PathVariable String nickName, @RequestHeader() String userId) {
        log.info("Rest API to find all messages by user {} and id {}", nickName, userId);
        return messageService.findBySenderUserName(nickName);
    }

    @GetMapping("/recipient-name/{nickName}")
    public List<Message> findAllMessagesByRecipientUserName(@PathVariable String nickName, @RequestHeader() String userId) {
        log.info("Rest API to find all messages received by user {} and id {}", nickName, userId);
        return messageService.findByRecipientUserName(nickName);
    }
}
