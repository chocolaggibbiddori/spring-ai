package chocola.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiServiceChainOfThoughtPrompt {

    private final ChatClient chatClient;

    public AiServiceChainOfThoughtPrompt(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Flux<String> chainOfThought(String question) {
        return chatClient
                .prompt("""
                        %s
                        한 걸음씩 생각해 봅시다.
                        
                        [예시]
                        질문: 제 동생이 2살일 때, 저는 그의 나이의 두 배였어요.
                        지금 저는 40살인데, 제 동생은 몇 살일까요? 한 걸음씩 생각해 봅시다.
                        
                        답변: 제 동생이 2살일 때, 저는 2 * 2 = 4살이었어요.
                        그때부터 2년 차이가 나며, 제가 더 나이가 많습니다.
                        지금 저는 40살이니, 제 동생은 40 - 4 = 38살이에요. 정답은 38살입니다.
                        """.formatted(question))
                .stream()
                .content();
    }
}
