package com.example.messagingapp.service.message;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.messagingapp.model.Conversation;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.model.dto.MessageDto;
import com.example.messagingapp.model.enums.MessageState;
import com.example.messagingapp.repository.MessageRepository;
import com.example.messagingapp.service.conversation.ConversationService;
import com.example.messagingapp.service.user.UserService;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${messaging.rabbitmq.exchange}")
    public String exchange;
    @Value("${messaging.rabbitmq.routing-key}")
    public String routingKey;

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ConversationService conversationService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Message sendMessage(MessageDto messageDto) {

        User senderUser = userService.findByName(messageDto.getSenderNickName());
        User recipientUser = userService.findByName(messageDto.getRecipientNickName());

        Conversation conversation;

        if (messageDto.getConversationId() > 0) {
            conversation = conversationService.getExistingConversation(senderUser.getId(), recipientUser.getId());
        }
        else {
            conversation = conversationService
                    .createConversation(Conversation.builder().senderUser(senderUser).recipientUser(recipientUser).build());
        }

        Message message = Message.builder()
                .message(messageDto.getMessage())
                .conversation(conversation)
                .recipientUser(recipientUser)
                .senderUser(senderUser)
                .state(MessageState.SENT)
                .sentAt(new Date()).build();

        Message savedMessage = messageRepository.save(message);

        if (savedMessage != null) {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedMessage);
        }

        log.info("Saved message {}", message.getMessage());
        return savedMessage;
    }

    @Override
    public MessageState deliveredMessage(long messageId) throws NotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found"));

        message.setState(MessageState.DELIVERED);
        Message savedMessage = messageRepository.save(message);

        return savedMessage.getState();
    }

    @Override
    public List<Message> findBySenderUserName(String userName) {
        User user = userService.findByName(userName);
        log.info("Get all messages by sender {}", userName);
        return messageRepository.findBySenderUserId(user.getId());
    }

    @Override
    public List<Message> findByRecipientUserName(String userName) {
        User user = userService.findByName(userName);
        log.info("Get all messages by recipient {}", userName);
        return messageRepository.findByRecipientUserId(user.getId());
    }

}
