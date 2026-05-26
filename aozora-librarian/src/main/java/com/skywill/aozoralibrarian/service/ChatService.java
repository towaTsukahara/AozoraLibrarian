package com.skywill.aozoralibrarian.service;

import com.skywill.aozoralibrarian.domain.entity.Character;
import com.skywill.aozoralibrarian.domain.entity.Conversation;
import com.skywill.aozoralibrarian.domain.entity.Message;
import com.skywill.aozoralibrarian.domain.enums.MessageRole;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private static final int HISTORY_LIMIT = 10;

    private final ChatClient.Builder chatClientBuilder;
    private final CharacterService characterService;
    private final ConversationService conversationService;

    public ChatService(ChatClient.Builder chatClientBuilder, CharacterService characterService,
            ConversationService conversationService) {
        this.chatClientBuilder = chatClientBuilder;
        this.characterService = characterService;
        this.conversationService = conversationService;
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

        // 4. LLMに渡すプロンプトを組み立てる
        Prompt prompt = buildPrompt(character, conversation);

        // 5.LLM呼び出し
        ChatClient chatClient = chatClientBuilder.build();
        String assistantResponse = chatClient.prompt(prompt).call().content();

        // 6.LLMの応答を保存
        conversationService.addMessage(conversation, MessageRole.ASSISTANT, assistantResponse);
        return assistantResponse;
    }

    private Prompt buildPrompt(Character character, Conversation conversation) {
        List<org.springframework.ai.chat.messages.Message> aiMessages = new ArrayList<>();

        aiMessages.add(new SystemMessage(character.getSystemPrompt()));

        List<Message> history = conversationService.findRecentMessages(
                conversation.getId(), HISTORY_LIMIT);

        for (Message msg : history) {
            switch (msg.getRole()) {
                case USER -> aiMessages.add(new UserMessage(msg.getContent()));
                case ASSISTANT -> aiMessages.add(new AssistantMessage(msg.getContent()));
                case SYSTEM -> {
                    aiMessages.add(new SystemMessage(msg.getContent()));
                }
            }
        }
        return new Prompt(aiMessages);
    }
}
