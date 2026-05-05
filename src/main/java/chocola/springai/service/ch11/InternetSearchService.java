package chocola.springai.service.ch11;

import chocola.springai.service.ch11.tool.InternetSearchTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class InternetSearchService {

    private final ChatClient chatClient;
    private final InternetSearchTools internetSearchTools;

    public InternetSearchService(ChatClient.Builder builder, InternetSearchTools internetSearchTools) {
        this.chatClient = builder.build();
        this.internetSearchTools = internetSearchTools;
    }

    public String chat(String question) {
        return chatClient
                .prompt(question)
                .tools(internetSearchTools)
                .call()
                .content();
    }
}
