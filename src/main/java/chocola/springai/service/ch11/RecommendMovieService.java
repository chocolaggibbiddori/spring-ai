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
                .system("""
                        질문에 대해 답변해 주세요.
                        사용자 ID가 존재하지 않을 경우, 진행을 멈추고,
                        '[LLM] 질문을 처리할 수 없습니다.'라고 답변해 주세요.
                        """)
                .tools(recommendMovieTools)
                .call()
                .content();
    }
}
