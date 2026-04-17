package chocola.springai.service.ch03;

import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class AiServiceZeroShotPrompt {

    private final ChatClient chatClient;
    private final PromptTemplate promptTemplate;

    public AiServiceZeroShotPrompt(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptions
                        .builder()
                        .temperature(0.0)
                        .maxTokens(4)
                        .build())
                .build();
        this.promptTemplate = new PromptTemplate("""
                영화 리뷰를 [긍정적, 중립적, 부정적] 중에서 하나로 분류하세요.
                레이블만 반환하세요.
                리뷰: {review}
                """);
    }

    public String zeroShotPrompt(String review) {
        Prompt prompt = promptTemplate.create(Map.of("review", review));
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
