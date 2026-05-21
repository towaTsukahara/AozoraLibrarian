package com.skywill.aozoralibrarian.controller;

import com.skywill.aozoralibrarian.service.ChatService;
import com.skywill.aozoralibrarian.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ChatPageController {

    private final ChatService chatService;
    private final SessionService sessionService;

    public ChatPageController(ChatService chatService, SessionService sessionService) {
        this.chatService = chatService;
        this.sessionService = sessionService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        // セッション開示に、Cookieを発行
        sessionService.resolveSessionId(request, response);
        return "chat";
    }

    @PostMapping("/chat")
    public String sendMessage(@RequestParam("message") String message, HttpServletRequest request, HttpServletResponse response, Model model){
        String sessionId = sessionService.resolveSessionId(request, response);
        String pagePath = "/";

        String librarianResponse = chatService.chat(sessionId, pagePath, message);

        model.addAttribute("userMessage", message);
        model.addAttribute("librarianResponse", librarianResponse);
        return "chat";
    }
    }
