package com.skywill.aozoralibrarian.controller;

import com.skywill.aozoralibrarian.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ChatPageController {
    
    private final ChatService chatService;

    public ChatPageController(ChatService chatService){
        this.chatService = chatService;
    }

    //TOP
    @GetMapping("/")
    public String index() {
        return "chat";
    }

    //メッセージの送信とAIからの返答表示先
    @PostMapping("/chat")
    public String sendMessage(@RequestParam("message") String message, Model model) {
        String response = chatService.askLibrarian(message);
        model.addAttribute("userMessage",message);
        model.addAttribute("librarianResponse",response);
        return "chat";
    }
    
    
}
