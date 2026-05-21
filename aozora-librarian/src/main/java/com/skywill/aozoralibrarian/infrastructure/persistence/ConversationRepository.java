package com.skywill.aozoralibrarian.infrastructure.persistence;

import com.skywill.aozoralibrarian.domain.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findBySessionIdAndCharacterId(String sessionId, Long characterId);
}