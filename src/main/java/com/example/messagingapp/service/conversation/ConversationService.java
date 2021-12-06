package com.example.messagingapp.service.conversation;

import java.util.List;

import com.example.messagingapp.model.Conversation;

import javassist.NotFoundException;

public interface ConversationService {

    Conversation findBySenderUserIdAndRecipientUserId(long senderUserId, long recipientUserId);

    Conversation createConversation(Conversation conversation);

    Conversation getExistingConversation(long senderUserId, long recipientUserId);

    List<Conversation> findBySenderUserIdOrRecipientUserId(long userId) throws NotFoundException;

    List<Conversation> findBySenderUserNameOrRecipientUserName(String userName) throws NotFoundException;
}
