package com.skywill.aozoralibrarian.web.dto;

import com.skywill.aozoralibrarian.domain.entity.Message;
import com.skywill.aozoralibrarian.domain.enums.MessageRole;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageDto {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final MessageRole role;
    private final String content;
    private final String timeLabel;

    private MessageDto(Long id, MessageRole role, String content, String timeLabel) {
        this.id = id;
        this.role = role;
        this.content = content;
        this.timeLabel = timeLabel;
    }

    public static MessageDto from(Message message) {
        return new MessageDto(message.getId(), message.getRole(), message.getContent(),
                formatTime(message.getCreatedAt()));
    }

    private static String formatTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(TIME_FORMATTER) : "";
    }

    public Long getId() {
        return id;
    }

    public MessageRole getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public boolean isUser() {
        return role == MessageRole.USER;
    }

    public boolean isAssistant() {
        return role == MessageRole.ASSISTANT;
    }
}