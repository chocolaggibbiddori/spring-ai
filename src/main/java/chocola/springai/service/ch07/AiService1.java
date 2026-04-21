package chocola.springai.service.ch07;

import chocola.springai.advisor.ch07.AdvisorA;
import chocola.springai.advisor.ch07.AdvisorB;
import chocola.springai.advisor.ch07.AdvisorC;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiService1 {

    private final ChatClient chatClient;

    public AiService1(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new AdvisorA(), new AdvisorB())
                .build();
    }

    public String advisorChain(String question) {
        return chatClient
                .prompt(question)
                .advisors(new AdvisorC())
                .call()
                .content();
    }

    public Flux<String> advisorChainStream(String question) {
        return chatClient
                .prompt(question)
                .advisors(new AdvisorC())
                .stream()
                .content();
    }
}
