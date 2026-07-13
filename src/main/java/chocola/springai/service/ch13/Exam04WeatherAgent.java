package chocola.springai.service.ch13;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class Exam04WeatherAgent {

    private static final String SYSTEM_PROMPT = """
            당신은 날씨 정보를 제공하는 전문 에이전트입니다.
            날씨 정보가 필요하면 반드시 Tool을 사용해 조회하세요.
            추측으로 답변하지 마세요.
            
            ## 사용 가능한 Tool
            1. getCurrentWeather: 특정 도시의 현재 날씨 정보를 조회
            2. getWeeklyForecast: 특정 도시의 주간 예보 조회
            3. getCurrentDate: 오늘 날짜 조회
            
            ## 규칙
            1. 사용자가 '주간', '이번 주', '예보', '5일'을 언급하면 getWeeklyForecast를 사용하세요.
            2. 사용자가 '오늘', '지금', '현재'를 언급하면 getCurrentWeather를 사용하세요.
            3. 그 외의 경우에는 질문의 의도에 맞게 적절한 Tool을 선택하거나,
               예보가 불가능한 시점에 대해서는 일반적인 기후 특성을 안내하세요.
            """;

    private final ChatClient chatClient;

    public Exam04WeatherAgent(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new CityValidationAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .order(1)
                        .build())
                .build();
    }

    public String execute(String conversationId, String userQuery) {
        return chatClient
                .prompt(userQuery)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(this)
                .call()
                .content();
    }

    @Tool(description = "오늘 날짜를 조회합니다.")
    public String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
    }

    @Tool(description = "특정 도시의 현재 날씨 정보를 조회합니다.")
    public String getCurrentWeather(@ToolParam(description = "도시 이름") String city) {
        return "%s의 현재 날씨는 맑고 23도입니다.".formatted(city);
    }

    @Tool(description = "특정 도시의 주간(5일) 날씨 예보를 조회합니다.")
    public String getWeeklyForecast(@ToolParam(description = "도시 이름") String city) {
        return "%s의 주간(5일) 날씨 예보는 흐리고 21도입니다.".formatted(city);
    }
}
