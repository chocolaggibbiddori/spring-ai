package chocola.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AiService {

    private final ChatModel chatModel;
    private final SystemMessage systemMessage;

    public AiService(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.systemMessage = new SystemMessage("사용자 질문에 대해 한국어로 답변을 해야 합니다.");
    }

    public String generateText(String question) {
        Prompt prompt = generatePrompt(question);

        return chatModel
                .call(prompt)
                .getResult()
                .getOutput()
                .getText();
    }

    public Flux<String> generateTextStream(String question) {
        Prompt prompt = generatePrompt(question);

        return chatModel
                .stream(prompt)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .mapNotNull(AssistantMessage::getText);
    }

    private Prompt generatePrompt(String question) {
        UserMessage userMessage = new UserMessage(question);
        return new Prompt(systemMessage, userMessage);
    }
}
