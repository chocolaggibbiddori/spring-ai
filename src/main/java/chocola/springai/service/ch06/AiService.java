package chocola.springai.service.ch06;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

@Service("aiService-ch06")
public class AiService {

    private final ChatClient chatClient;
    private final ImageModel imageModel;

    public AiService(ChatClient.Builder chatClientBuilder, ImageModel imageModel) {
        this.chatClient = chatClientBuilder
                .defaultSystem("당신은 이미지 분석 전문가입니다. 사용자 질문에 맞게 이미지를 분석하고 답변을 한국어로 하세요.")
                .build();
        this.imageModel = imageModel;
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

    public String generateImage(String description) {
        ImageMessage imageMessage = new ImageMessage(koToEn(description));
        OpenAiImageOptions options = OpenAiImageOptions
                .builder()
                .model("gpt-image-1")
                .quality("low")
                .width(1536)
                .height(1024)
                .N(1)
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(imageMessage, options);

        return imageModel
                .call(imagePrompt)
                .getResult()
                .getOutput()
                .getB64Json();
    }

    private String koToEn(String text) {
        return chatClient
                .prompt("당신은 번역사입니다. 아래 한글 문장을 영어 문장으로 번역해 주세요.")
                .user(text)
                .call()
                .content();
    }
}
