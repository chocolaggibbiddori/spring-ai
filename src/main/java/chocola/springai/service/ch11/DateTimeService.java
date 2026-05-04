package chocola.springai.service.ch11;

import chocola.springai.service.ch11.tools.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class DateTimeService {

    private final ChatClient chatClient;
    private final DateTimeTools dateTimeTools;

    public DateTimeService(ChatClient.Builder builder, DateTimeTools dateTimeTools) {
        this.chatClient = builder.build();
        this.dateTimeTools = dateTimeTools;
    }

    public String chat(String question) {
        return chatClient
                .prompt(question)
                .tools(dateTimeTools)
                .call()
                .content();
    }
}
