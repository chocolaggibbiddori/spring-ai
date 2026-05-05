package chocola.springai.service.ch11;

import chocola.springai.service.ch11.tools.RecommendMovieTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RecommendMovieService {

    private final ChatClient chatClient;
    private final RecommendMovieTools recommendMovieTools;

    public RecommendMovieService(ChatClient.Builder builder, RecommendMovieTools recommendMovieTools) {
        this.chatClient = builder.build();
        this.recommendMovieTools = recommendMovieTools;
    }

    public String chat(String question) {
        return chatClient
                .prompt(question)
                .tools(recommendMovieTools)
                .call()
                .content();
    }
}
