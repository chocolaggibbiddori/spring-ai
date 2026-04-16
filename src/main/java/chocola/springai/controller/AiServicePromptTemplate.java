package chocola.springai.controller;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AiServicePromptTemplate {

    private final ChatClient chatClient;
    private final SystemPromptTemplate systemPromptTemplate;
    private final PromptTemplate promptTemplate;

    public AiServicePromptTemplate(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.systemPromptTemplate = new SystemPromptTemplate("""
                답변을 생성할 때 HTML과 CSS를 사용해서 파란 글자로 출력하세요.
                <span> 태그 안에 들어갈 내용만 출력하세요.
                """);
        this.promptTemplate = new PromptTemplate("다음 한국어 문장을 {language}로 번역해 주세요.\n문장: {statement}");
    }

    public Flux<String> promptTemplate1(String statement, String language) {
        Prompt prompt = promptTemplate.create(Map.of("statement", statement, "language", language));
        return chatClient
                .prompt(prompt)
                .stream()
                .content();
    }

    public Flux<String> promptTemplate2(String statement, String language) {
        Message systemMessage = systemPromptTemplate.createMessage();
        Message userMessage = promptTemplate.createMessage(Map.of("statement", statement, "language", language));

        return chatClient
                .prompt()
                .messages(systemMessage, userMessage)
                .stream()
                .content();
    }

    public Flux<String> promptTemplate3(String statement, String language) {
        String systemMessageText = systemPromptTemplate.render();
        String userMessageText = promptTemplate.render(Map.of("statement", statement, "language", language));

        return chatClient
                .prompt()
                .system(systemMessageText)
                .user(userMessageText)
                .stream()
                .content();
    }
}
