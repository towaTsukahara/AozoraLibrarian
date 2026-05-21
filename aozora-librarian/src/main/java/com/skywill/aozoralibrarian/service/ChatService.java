package com.skywill.aozoralibrarian.service;

import com.skywill.aozoralibrarian.AozoraLibrarianApplication;
import com.skywill.aozoralibrarian.domain.entity.Character;
import com.skywill.aozoralibrarian.domain.entity.Conversation;
import com.skywill.aozoralibrarian.domain.enums.MessageRole;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final AozoraLibrarianApplication aozoraLibrarianApplication;
    private final ChatClient.Builder chatClientBuilder;
    private final CharacterService characterService;
    private final ConversationService conversationService;

    public ChatService(ChatClient.Builder chatClientBuilder, CharacterService characterService,
            ConversationService conversationService, AozoraLibrarianApplication aozoraLibrarianApplication) {

        this.chatClientBuilder = chatClientBuilder;
        this.characterService = characterService;
        this.conversationService = conversationService;
        this.aozoraLibrarianApplication = aozoraLibrarianApplication;
    }

    /*
     * 指定されたページのキャラと、指定されたセッションで会話
     * USERのメッセージとASSISTANT応答 DBに保存
     */

    public String chat(String sessionId, String pagePath, String userMessage) {

        // 1.ページからキャラを取得
        Character character = characterService.findByPagePath(pagePath);

        // 2.会話セッションを取得or新規作成
        Conversation conversation = conversationService.findOrCreate(sessionId, character);

        // 3.USERメッセージを保存
        conversationService.addMessage(conversation, MessageRole.USER, userMessage);

        // 4.LLM呼び出し
        ChatClient chatClient = chatClientBuilder
                .defaultSystem(character.getSystemPrompt())
                .build();

        String assistantResponse = chatClient.prompt()
                .user(userMessage)
                .call()
                .content();

        // 5.LLMの応答を保存
        conversationService.addMessage(conversation, MessageRole.ASSISTANT, assistantResponse);
        return assistantResponse;
    }
}
