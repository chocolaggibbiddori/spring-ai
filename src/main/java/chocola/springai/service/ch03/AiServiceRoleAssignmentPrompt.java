package chocola.springai.service.ch03;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiServiceRoleAssignmentPrompt {

    private final ChatClient chatClient;

    public AiServiceRoleAssignmentPrompt(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        당신이 여행 가이드 역할을 해 주었으면 좋겠습니다.
                        아래 요청사항에서 위치를 알려주면, 근처에 있는 3곳을 제안해 주고,
                        이유를 달아주세요. 경우에 따라서 방문하고 싶은 장소 유형을
                        제공할 수도 있습니다.
                        """)
                .defaultOptions(ChatOptions
                        .builder()
                        .temperature(1.0)
                        .maxTokens(1000)
                        .build())
                .build();
    }

    public Flux<String> roleAssignment(String requirements) {
        return chatClient
                .prompt(requirements)
                .stream()
                .content();
    }
}
