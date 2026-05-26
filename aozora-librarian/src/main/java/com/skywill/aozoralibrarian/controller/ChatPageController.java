package com.skywill.aozoralibrarian.controller;

import com.skywill.aozoralibrarian.domain.entity.Character;
import com.skywill.aozoralibrarian.domain.entity.Conversation;
import com.skywill.aozoralibrarian.domain.entity.Message;
import com.skywill.aozoralibrarian.service.CharacterService;
import com.skywill.aozoralibrarian.service.ChatService;
import com.skywill.aozoralibrarian.service.ConversationService;
import com.skywill.aozoralibrarian.service.SessionService;
import com.skywill.aozoralibrarian.web.dto.MessageDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.List;

@Controller
public class ChatPageController {

    private final ChatService chatService;
    private final SessionService sessionService;
    private final CharacterService characterService;
    private final ConversationService conversationService;

    public ChatPageController(ChatService chatService, SessionService sessionService, CharacterService characterService,
            ConversationService conversationService) {
        this.chatService = chatService;
        this.sessionService = sessionService;
        this.characterService = characterService;
        this.conversationService = conversationService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String sessionId = sessionService.getSessionIdIfExists(request);
        Character character = characterService.findByPagePath("/");

        List<MessageDto> messages = Collections.emptyList();
        if (sessionId != null) {
            Conversation conversation = conversationService.findOrCreate(sessionId, character);
            List<Message> entities = conversationService.findAllMessages(conversation.getId());
            messages = entities.stream().map(MessageDto::from).toList();
        }

        model.addAttribute("character", character);
        model.addAttribute("messages", messages);
        return "chat";
    }

    @PostMapping("/chat")
    public RedirectView sendMessage(@RequestParam("message") String message, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = sessionService.resolveSessionId(request, response);
        String pagePath = "/";
        chatService.chat(sessionId, pagePath, message);
        return new RedirectView("/");
    }
}
