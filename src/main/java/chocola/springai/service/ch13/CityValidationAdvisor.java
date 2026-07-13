package chocola.springai.service.ch13;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Set;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.core.Ordered;

public class CityValidationAdvisor implements CallAdvisor {

    private static final Set<String> KOREAN_CITIES = Set.of(
            "서울", "부산", "인천", "대구", "광주", "대전", "울산", "수원", "제주"
    );

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        String userText = chatClientRequest
                .prompt()
                .getUserMessages()
                .stream()
                .map(UserMessage::getText)
                .collect(joining());

        boolean valid = KOREAN_CITIES
                .stream()
                .anyMatch(userText::contains);

        if (!valid) {
            AssistantMessage assistantMessage = new AssistantMessage("한국 도시만 날씨 정보를 지원합니다.");
            Generation generation = new Generation(assistantMessage);
            ChatResponse chatResponse = new ChatResponse(List.of(generation));

            return ChatClientResponse
                    .builder()
                    .chatResponse(chatResponse)
                    .build();
        }

        return callAdvisorChain.nextCall(chatClientRequest);
    }

    @Override
    public String getName() {
        return "cityValidationAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
