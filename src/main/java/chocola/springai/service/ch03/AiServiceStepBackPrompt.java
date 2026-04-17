package chocola.springai.service.ch03;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AiServiceStepBackPrompt {

    private final ChatClient chatClient;
    private final ObjectMapper om;

    public AiServiceStepBackPrompt(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.om = new ObjectMapper();
    }

    public String stepBackPrompt(String question) throws JsonProcessingException {
        String questions = chatClient
                .prompt("""
                        사용자 질문을 처리하기 Step-Back 프롬프트 기법을 사용하려고 합니다.
                        사용자 질문을 단계별 질문들로 재구성해 주세요.
                        맨 마지막 질문은 사용자 질문과 일치해야 합니다.
                        단계별 질문을 항목으로 하는 JSON 배열로 출력해 주세요.
                        예시: ["...", "...", "...", ...]
                        사용자 질문: %s
                        """.formatted(question))
                .call()
                .content();

        if (questions == null) {
            return null;
        }

        String json = questions.substring(questions.indexOf("["), questions.lastIndexOf("]") + 1);
        log.info("json: {}", json);

        List<String> questionList = om.readValue(json, new TypeReference<>() {
        });
        String[] answers = new String[questionList.size()];
        for (int i = 0; i < questionList.size(); i++) {
            question = questionList.get(i);

            String answer = getAnswer(question, answers);
            answers[i] = answer;

            log.info("단계 {} - 질문: {}, 답변: {}", i + 1, question, answer);
        }

        return answers[answers.length - 1];
    }

    private String getAnswer(String question, String... prevAnswers) {
        StringBuilder context = new StringBuilder();

        for (String prevAnswer : prevAnswers) {
            context.append(Objects.requireNonNullElse(prevAnswer, ""));
        }

        return chatClient
                .prompt("""
                        %s
                        문맥: %s
                        """.formatted(question, context.toString()))
                .call()
                .content();
    }
}
