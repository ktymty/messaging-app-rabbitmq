package com.example.messagingapp.service.message;

import java.util.List;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.dto.MessageDto;
import com.example.messagingapp.model.enums.MessageState;

import javassist.NotFoundException;

public interface MessageService {

    Message sendMessage(MessageDto messageDto) throws NotFoundException;

    MessageState deliveredMessage(long messageId) throws NotFoundException;

    List<Message> findBySenderUserName(String userName);

    List<Message> findByRecipientUserName(String userName);
}
