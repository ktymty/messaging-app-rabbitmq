package com.example.messagingapp.service.conversation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.messagingapp.model.Conversation;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.ConversationRepository;
import com.example.messagingapp.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Conversation findBySenderUserIdAndRecipientUserId(long senderUserId, long recipientUserId) {
        log.info("Find conversation by senderUserId {} or recipientUserId {}", senderUserId, recipientUserId);
        return conversationRepository.findBySenderUserIdAndRecipientUserId(senderUserId, recipientUserId);
    }

    @Override
    @Transactional
    public Conversation createConversation(Conversation conversation) {
        log.info("Create conversation {}", conversation);
        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional(readOnly = true)
    public Conversation getExistingConversation(long senderUserId, long recipientUserId) {
        log.info("Get existing conversation by senderUserId {} or recipientUserId {}", senderUserId, recipientUserId);
        Conversation sendersConversation = findBySenderUserIdAndRecipientUserId(senderUserId, recipientUserId);
        Conversation recipientsConversation = findBySenderUserIdAndRecipientUserId(recipientUserId, senderUserId);
        return sendersConversation != null ? sendersConversation : recipientsConversation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> findBySenderUserIdOrRecipientUserId(long userId) {
        log.info("Find conversations by userId {}", userId);
        userService.getById(userId);
        return conversationRepository.findBySenderUserIdOrRecipientUserId(userId, userId);
    }

    @Override
    public List<Conversation> findBySenderUserNameOrRecipientUserName(String userName) {
        log.info("Find conversations by userName {}", userName);
        User user = userService.findByName(userName);
        return conversationRepository.findBySenderUserIdOrRecipientUserId(user.getId(), user.getId());
    }
}
