package chocola.springai.service.ch03;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AiServiceSelfConsistency {

    private static final int REQUEST_COUNT = 5;

    private final ChatClient chatClient;
    private final PromptTemplate promptTemplate;

    public AiServiceSelfConsistency(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptions
                        .builder()
                        .temperature(1.0)
                        .build())
                .build();
        this.promptTemplate = new PromptTemplate("""
                다음 내용을 IMPORTANT, NOT_IMPORTANT 둘 중 하나로 분류해 주세요.
                레이블만 반환하세요.
                내용: {content}
                """);
    }

    public String selfConsistency(String content) {
        Prompt prompt = promptTemplate.create(Map.of("content", content));
        int importantCount = 0;
        int notImportantCount = 0;

        for (int i = 0; i < REQUEST_COUNT; i++) {
            String answer = chatClient
                    .prompt(prompt)
                    .call()
                    .content();

            log.info("{}: {}", i, answer);

            if ("IMPORTANT".equals(answer)) {
                ++importantCount;
            } else {
                ++notImportantCount;
            }
        }

        return importantCount > notImportantCount ? "IMPORTANT" : "NOT_IMPORTANT";
    }
}
