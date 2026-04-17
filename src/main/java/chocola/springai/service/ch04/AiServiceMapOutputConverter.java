package chocola.springai.service.ch04;

import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.stereotype.Service;

@Service
public class AiServiceMapOutputConverter {

    private final ChatClient chatClient;
    private final MapOutputConverter mapOutputConverter;

    public AiServiceMapOutputConverter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.mapOutputConverter = new MapOutputConverter();
    }

    public Map<String, Object> mapOutputConverterHighLevel(String hotel) {
        PromptTemplate promptTemplate = new PromptTemplate("호텔 {hotel}에 대해 정보를 알려주세요.");
        Prompt prompt = promptTemplate.create(Map.of("hotel", hotel));
        Map<String, Object> result = chatClient
                .prompt(prompt)
                .call()
                .entity(mapOutputConverter);

        return result == null ? Map.of() : result;
    }
}
