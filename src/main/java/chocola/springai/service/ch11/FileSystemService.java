package chocola.springai.service.ch11;

import chocola.springai.service.ch11.tool.FileSystemTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class FileSystemService {

    private final ChatClient chatClient;
    private final FileSystemTools fileSystemTools;

    public FileSystemService(ChatClient.Builder builder, ChatMemory chatMemory, FileSystemTools fileSystemTools) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .build())
                .defaultSystem("파일, 디렉토리 관련 질문은 반드시 도구를 사용하세요.")
                .build();
        this.fileSystemTools = fileSystemTools;
    }

    public String chat(String question, String conversationId) {
        return chatClient
                .prompt(question)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(fileSystemTools)
                .call()
                .content();
    }
}
