package chocola.springai.service.ch04;

import chocola.springai.dto.ch04.Hotel;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class AiServiceParameterizedTypeReference {

    private final ChatClient chatClient;
    private final BeanOutputConverter<List<Hotel>> beanOutputConverter;

    public AiServiceParameterizedTypeReference(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.beanOutputConverter = new BeanOutputConverter<>(new ParameterizedTypeReference<>() {
        });
    }

    public List<Hotel> genericBeanOutputConverterHighLevel(String cities) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                다음 도시들에서 유명한 호텔 3개를 출력하세요.
                {cities}
                """);
        Prompt prompt = promptTemplate.create(Map.of("cities", cities));

        return chatClient
                .prompt(prompt)
                .call()
                .entity(beanOutputConverter);
    }
}
