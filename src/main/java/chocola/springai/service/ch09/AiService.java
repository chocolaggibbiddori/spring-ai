package chocola.springai.service.ch09;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service("aiService-ch09")
public class AiService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public AiService(ChatClient.Builder chatClientBuilder,
                     ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder.build();
        this.chatMemory = chatMemory;
    }

    public String chatInMemory(String userText, String conversationId) {
        return chatClient
                .prompt(userText)
                .advisors(PromptChatMemoryAdvisor
                                .builder(chatMemory)
                                .order(1)
                                .build(),
                        new SimpleLoggerAdvisor(2))
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
