package chocola.springai.service.ch07;

import chocola.springai.advisor.ch07.MaxCharLengthAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

@Service
public class AiService3 {

    private final ChatClient chatClient;

    public AiService3(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MaxCharLengthAdvisor(1),
                        new SimpleLoggerAdvisor(2)
                )
                .build();
    }

    public String advisorLogging(String question) {
        return chatClient
                .prompt(question)
                .advisors(advisor -> advisor.param(MaxCharLengthAdvisor.MAX_CHAR_LENGTH, 10))
                .call()
                .content();
    }
}
