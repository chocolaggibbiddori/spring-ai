package chocola.springai.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AiServiceByChatClient {

    private final ChatClient chatClient;

    public AiServiceByChatClient(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
                .build();
    }

    public String generateText(String question) {
        return chatClient
                .prompt(question)
                .call()
                .content();
    }

    public Flux<String> generateTextStream(String question) {
        return chatClient
                .prompt(question)
                .stream()
                .content();
    }
}
