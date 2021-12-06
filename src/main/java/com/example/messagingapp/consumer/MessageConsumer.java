package com.example.messagingapp.consumer;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.service.message.MessageService;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "${messaging.rabbitmq.queue}")
    public void consumeMessageFromQueue(Message message) {
        log.info("Consumed Message ====> " + message.toString());

        CompletableFuture.runAsync(() -> {
            try {
                messageService.deliveredMessage(message.getId());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });

        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getRecipientUser().getId()), "/queue/messages", message);

    }
}
