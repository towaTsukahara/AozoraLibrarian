package com.skywill.aozoralibrarian.service;

import com.skywill.aozoralibrarian.domain.entity.Character;
import com.skywill.aozoralibrarian.domain.entity.Conversation;
import com.skywill.aozoralibrarian.domain.entity.Message;
import com.skywill.aozoralibrarian.domain.enums.MessageRole;
import com.skywill.aozoralibrarian.infrastructure.persistence.ConversationRepository;
import com.skywill.aozoralibrarian.infrastructure.persistence.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationService(ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    /* sessionIdとcharacterの組み合わせで会話を探して、なければ新規の履歴を作成 */
    @Transactional
    public Conversation findOrCreate(String sessionId, Character character) {
        return conversationRepository
                .findBySessionIdAndCharacterId(sessionId, character.getId())
                .orElseGet(() -> {
                    Conversation conversation = new Conversation();
                    conversation.setSessionId(sessionId);
                    conversation.setCharacter(character);
                    return conversationRepository.save(conversation);
                });
    }

    @Transactional
    public Message addMessage(Conversation conversation, MessageRole role, String content) {
        Message message = new Message();
        message.setConversation(conversation);
        message.setRole(role);
        message.setContent(content);
        return messageRepository.save(message);
    }
}
