package chocola.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;

@Service
public class AiServiceFewShotPrompt {

    private final ChatClient chatClient;

    public AiServiceFewShotPrompt(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptions
                        .builder()
                        .temperature(0.0)
                        .maxTokens(300)
                        .build())
                .build();
    }

    public String fewShotPrompt(String order) {
        String prompt = """
                고객 주문을 유효한 JSON 형식으로 바꿔주세요.
                추가 설명은 포함하지 마세요.
                
                예시 1:
                작은 피자 하나, 치즈랑 토마토 소스, 페퍼로니 올려서 주세요.
                JSON 응답:
                {
                    "size": "small",
                    "type": "normal",
                    "ingredients": ["cheese", "tomato sauce", "pepperoni"]
                }
                
                예시 2:
                큰 피자 하나, 토마토 소스랑 바질, 모짜렐라 올려서 주세요.
                JSON 응답:
                {
                    "size": "large",
                    "type": "normal",
                    "ingredients": ["tomato sauce", "basil", "mozzarella"]
                }
                
                고객 주문: %s
                """.formatted(order);

        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
