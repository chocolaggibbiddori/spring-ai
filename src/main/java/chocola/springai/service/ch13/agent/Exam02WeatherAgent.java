package chocola.springai.service.ch13.agent;

import chocola.springai.service.ch13.advisor.CityValidationAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class Exam02WeatherAgent {

    private static final String SYSTEM_PROMPT = """
            당신은 날씨 정보를 제공하는 전문 에이전트입니다.
            날씨 정보가 필요하면 반드시 Tool을 사용해 조회하세요.
            추측으로 답변하지 마세요.
            
            ## 사용 가능한 Tool
            1. getWeather: 특정 도시의 현재 날씨 정보를 조회
            """;

    private final ChatClient chatClient;

    public Exam02WeatherAgent(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new CityValidationAdvisor())
                .build();
    }

    public String execute(String userQuery) {
        return chatClient
                .prompt(userQuery)
                .tools(this)
                .call()
                .content();
    }

    @Tool(description = "특정 도시의 현재 날씨 정보를 조회합니다.")
    public String getWeather(@ToolParam(description = "도시 이름") String city) {
        return "%s의 현재 날씨는 맑고 23도입니다.".formatted(city);
    }
}
