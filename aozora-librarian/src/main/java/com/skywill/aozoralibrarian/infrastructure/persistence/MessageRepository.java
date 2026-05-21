package com.skywill.aozoralibrarian.infrastructure.persistence;

import com.skywill.aozoralibrarian.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}