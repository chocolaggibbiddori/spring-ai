package chocola.springai.service.ch04;

import chocola.springai.dto.ch04.Hotel;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

@Service
public class AiServiceBeanOutputConverter {

    private final ChatClient chatClient;
    private final BeanOutputConverter<Hotel> beanOutputConverter;

    public AiServiceBeanOutputConverter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.beanOutputConverter = new BeanOutputConverter<>(Hotel.class);
    }

    public Hotel beanOutputConverterHighLevel(String city) {
        PromptTemplate promptTemplate = new PromptTemplate("{city}에서 유명한 호텔 목록 5개를 출력하세요.");
        Prompt prompt = promptTemplate.create(Map.of("city", city));

        return chatClient
                .prompt(prompt)
                .call()
                .entity(beanOutputConverter);
    }
}
