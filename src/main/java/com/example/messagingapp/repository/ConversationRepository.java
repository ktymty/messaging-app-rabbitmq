package com.example.messagingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.messagingapp.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Conversation findBySenderUserIdAndRecipientUserId(long senderUserId, long recipientUserId);

    List<Conversation> findBySenderUserIdOrRecipientUserId(long senderUserId, long recipientUserId);
}
