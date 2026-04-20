package chocola.springai.service.ch06;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

@Service("aiService-ch06")
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("당신은 이미지 분석 전문가입니다. 사용자 질문에 맞게 이미지를 분석하고 답변을 한국어로 하세요.")
                .build();
    }

    public Flux<String> imageAnalysis(String question, String contentType, Resource resource) {
        Media media = new Media(MimeType.valueOf(contentType), resource);
        UserMessage userMessage = UserMessage
                .builder()
                .text(question)
                .media(media)
                .build();

        return chatClient
                .prompt()
                .messages(userMessage)
                .stream()
                .content();
    }
}
