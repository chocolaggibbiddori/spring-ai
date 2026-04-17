package chocola.springai.service.ch04;

import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.stereotype.Service;

@Service
public class AiServiceListOutputConverter {

    private final ChatClient chatClient;
    private final ListOutputConverter listOutputConverter;

    public AiServiceListOutputConverter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.listOutputConverter = new ListOutputConverter();
    }

    public List<String> listOutputConverterLowLevel(String city) {
        PromptTemplate promptTemplate = new PromptTemplate("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}");
        Prompt prompt = promptTemplate.create(Map.of("city", city, "format", listOutputConverter.getFormat()));
        String answer = chatClient
                .prompt(prompt)
                .call()
                .content();

        if (answer == null) {
            return List.of();
        }

        return listOutputConverter.convert(answer);
    }

    public List<String> listOutputConverterHighLevel(String city) {
        PromptTemplate promptTemplate = new PromptTemplate("{city}에서 유명한 호텔 목록 5개를 출력하세요.");
        Prompt prompt = promptTemplate.create(Map.of("city", city));
        List<String> result = chatClient
                .prompt(prompt)
                .call()
                .entity(listOutputConverter);

        return result == null ? List.of() : result;
    }
}
