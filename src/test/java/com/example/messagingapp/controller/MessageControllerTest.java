package com.example.messagingapp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.messagingapp.model.Conversation;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.model.dto.MessageDto;
import com.example.messagingapp.model.enums.MessageState;
import com.example.messagingapp.service.message.MessageService;

@WebMvcTest(MessageController.class)
@DisplayName("Message Controller")
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    private MessageDto messageDto;
    private Message message;

    @BeforeEach
    void setUp() {
        Date date = Date.from(Instant.parse("2021-11-29T15:30:00.00Z"));
        User senderUser = User.builder().id(1L).name("tonyStark").build();
        User recipientUser = User.builder().id(2L).name("captainAmerica").build();
        Conversation conversation = Conversation.builder().id(1L).senderUser(senderUser).recipientUser(recipientUser).build();

        messageDto = MessageDto.builder().senderNickName("tonyStark").recipientNickName("captainAmerica").message("avengers assemble").build();
        message = Message.builder().id(1L).message("avengers assemble").senderUser(senderUser).recipientUser(recipientUser).conversation(conversation)
                .state(MessageState.SENT).sentAt(date).build();
    }

    @AfterEach
    void tearDown() {
        messageDto = null;
        message = null;
    }

    @Test
    @DisplayName("should send message")
    void sendMessage() throws Exception {
        when(messageService.sendMessage(messageDto)).thenReturn(message);
        this.mockMvc.perform(
                post("/v1/messages/send-message").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                        "   \"message\":\"avengers assemble\",\n" +
                        "   \"senderNickName\":\"tonyStark\",\n" +
                        "   \"recipientNickName\":\"captainAmerica\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":1,\"message\":\"avengers assemble\",\"state\":\"SENT\",\"senderUser\":{\"id\":1,\"name\":\"tonyStark\"},\"recipientUser\":{\"id\":2,\"name\":\"captainAmerica\"},\"conversation\":{\"id\":1,\"senderUser\":{\"id\":1,\"name\":\"tonyStark\"},\"recipientUser\":{\"id\":2,\"name\":\"captainAmerica\"}},\"sentAt\":\"2021-11-29T15:30:00.000+00:00\"}"));
    }

    @Test
    void findAllMessagesBySenderUserName() throws Exception {
        when(messageService.findBySenderUserName("tonyStark")).thenReturn(Collections.singletonList(message));
        this.mockMvc.perform(get("/v1/messages/sender-name/tonyStark").header("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":1,\"message\":\"avengers assemble\",\"state\":\"SENT\",\"senderUser\":{\"id\":1,\"name\":\"tonyStark\"},\"recipientUser\":{\"id\":2,\"name\":\"captainAmerica\"},\"conversation\":{\"id\":1,\"senderUser\":{\"id\":1,\"name\":\"tonyStark\"},\"recipientUser\":{\"id\":2,\"name\":\"captainAmerica\"}},\"sentAt\":\"2021-11-29T15:30:00.000+00:00\"}]"));
    }

    @Test
    void findAllMessagesByRecipientUserName() throws Exception {
        when(messageService.findByRecipientUserName("captainAmerica")).thenReturn(Collections.singletonList(message));
        this.mockMvc.perform(get("/v1/messages/recipient-name/captainAmerica").header("userId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":1,\"message\":\"avengers assemble\",\"state\":\"SENT\",\"senderUser\":{\"id\":1,\"name\":\"tonyStark\"},\"recipientUser\":{\"id\":2,\"name\":\"captainAmerica\"},\"conversation\":{\"id\":1,\"senderUser\":{\"id\":1,\"name\":\"tonyStark\"},\"recipientUser\":{\"id\":2,\"name\":\"captainAmerica\"}},\"sentAt\":\"2021-11-29T15:30:00.000+00:00\"}]"));

    }
}