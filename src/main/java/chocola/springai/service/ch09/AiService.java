package chocola.springai.service.ch09;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service("aiService-ch09")
public class AiService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;

    public AiService(ChatClient.Builder chatClientBuilder,
                     ChatMemory chatMemory,
                     VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
    }

    public String chatInMemory(String userText, String conversationId) {
        return chatClient
                .prompt(userText)
                .advisors(PromptChatMemoryAdvisor
                                .builder(MessageWindowChatMemory
                                        .builder()
                                        .chatMemoryRepository(new InMemoryChatMemoryRepository())
                                        .build())
                                .order(1)
                                .build(),
                        new SimpleLoggerAdvisor(2))
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public String chatVectorStore(String userText, String conversationId) {
        return chatClient
                .prompt(userText)
                .advisors(VectorStoreChatMemoryAdvisor
                                .builder(vectorStore)
                                .order(1)
                                .build(),
                        new SimpleLoggerAdvisor(2))
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public String chatRdbms(String userText, String conversationId) {
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
