package com.example.messagingapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    @NonNull
    private String message;

    @NonNull
    private String senderNickName;

    @NonNull
    private String recipientNickName;

    private long conversationId;
}
