package chocola.springai.service.ch04;

import chocola.springai.dto.ch04.ReviewClassification;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;

@Service
public class AiServiceSystemMessage {

    private final ChatClient chatClient;

    public AiServiceSystemMessage(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("영화 리뷰를 [POSITIVE, NEUTRAL, NEGATIVE] 중에서 하나로 분류하고, 유효한 JSON을 반환하세요.")
                .defaultOptions(ChatOptions
                        .builder()
                        .temperature(0.0)
                        .build())
                .build();
    }

    public ReviewClassification classifyReview(String review) {
        return chatClient
                .prompt(review)
                .call()
                .entity(ReviewClassification.class);
    }
}
