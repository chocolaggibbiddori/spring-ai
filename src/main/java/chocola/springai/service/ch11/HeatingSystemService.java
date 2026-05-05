package chocola.springai.service.ch11;

import chocola.springai.service.ch11.tools.HeatingSystemTools;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class HeatingSystemService {

    private final ChatClient chatClient;
    private final HeatingSystemTools heatingSystemTools;

    public HeatingSystemService(ChatClient.Builder chatClientBuilder, HeatingSystemTools heatingSystemTools) {
        this.chatClient = chatClientBuilder.build();
        this.heatingSystemTools = heatingSystemTools;
    }

    public String chat(String question) {
        return chatClient
                .prompt(question)
                .system("""
                        현재 온도가 사용자가 원하는 온도 이상이라면 난방 시스템을 중지하세요.
                        그렇지 않으면 난방 시스템을 가동시켜주세요.
                        """)
                .tools(heatingSystemTools)
                .toolContext(Map.of("controlKey", "heatingSystemKey"))
                .call()
                .content();
    }
}
