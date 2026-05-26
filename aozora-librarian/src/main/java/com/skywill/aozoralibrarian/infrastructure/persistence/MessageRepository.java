package com.skywill.aozoralibrarian.infrastructure.persistence;

import com.skywill.aozoralibrarian.domain.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /* 直近の会話のログを取得する */
    @Query("SELECT m FROM Message m " + "WHERE m.conversation.id = :conversationId " + "ORDER BY m.createdAt DESC")
    List<Message> findRecentByConversationId(@Param("conversationId") Long conversationId, Pageable pageable);
}