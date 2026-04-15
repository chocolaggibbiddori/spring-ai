package chocola.springai.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AiService {

    private final ChatModel chatModel;
    private final ChatOptions chatOptions;
    private final SystemMessage systemMessage;

    public AiService(ChatModel chatModel, OpenAiChatProperties openAiChatProperties) {
        this.chatModel = chatModel;
        this.chatOptions = openAiChatProperties.getOptions();
        this.systemMessage = new SystemMessage("사용자 질문에 대해 한국어로 답변을 해야 합니다.");
    }

    public String generateText(String question) {
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), chatOptions);

        return chatModel
                .call(prompt)
                .getResult()
                .getOutput()
                .getText();
    }
}
