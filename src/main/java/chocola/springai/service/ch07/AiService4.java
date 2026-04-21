package chocola.springai.service.ch07;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.stereotype.Service;

@Service
public class AiService4 {

    private final ChatClient chatClient;

    public AiService4(ChatClient.Builder chatClientBuilder) {
        SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(
                List.of("욕설", "계좌번호", "폭력", "폭탄"),
                "해당 내용은 적절하지 않습니다.",
                1);
        this.chatClient = chatClientBuilder
                .defaultAdvisors(safeGuardAdvisor)
                .build();
    }

    public String advisorSafeGuard(String question) {
        return chatClient
                .prompt(question)
                .call()
                .content();
    }
}
