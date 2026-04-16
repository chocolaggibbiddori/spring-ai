package chocola.springai.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AiServiceMultiMessage {

    private final ChatClient chatClient;
    private final SystemMessage systemMessage;

    public AiServiceMultiMessage(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.systemMessage = new SystemMessage("당신은 AI 비서입니다. 제공되는 지난 대화 내용을 보고 우선적으로 답변해 주세요.");
    }

    public String multiMessage(String question, List<Message> chatMemory) {
        if (chatMemory.isEmpty()) {
            chatMemory.add(systemMessage);
        }

        log.info("chatMemory: {}", chatMemory);

        ChatResponse chatResponse = chatClient
                .prompt()
                .messages(chatMemory)
                .user(question)
                .call()
                .chatResponse();

        if (chatResponse == null) {
            return null;
        }

        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        chatMemory.add(new UserMessage(question));
        chatMemory.add(assistantMessage);

        return assistantMessage.getText();
    }
}
