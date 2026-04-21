package chocola.springai.service.ch07;

import chocola.springai.advisor.ch07.MaxCharLengthAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService2 {

    private final ChatClient chatClient;

    public AiService2(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new MaxCharLengthAdvisor(1))
                .build();
    }

    public String advisorContext(String question) {
        return chatClient
                .prompt(question)
                .advisors(advisor -> advisor.param(MaxCharLengthAdvisor.MAX_CHAR_LENGTH, 10))
                .call()
                .content();
    }
}
