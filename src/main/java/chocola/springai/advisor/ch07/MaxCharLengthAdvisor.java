package chocola.springai.advisor.ch07;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;

public class MaxCharLengthAdvisor implements CallAdvisor {

    public static final String MAX_CHAR_LENGTH = "maxCharLength";
    private static final Integer DEFAULT_MAX_CHAR_LENGTH = 300;

    private final int order;

    public MaxCharLengthAdvisor(int order) {
        this.order = order;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientRequest mutatedRequest = augmentPrompt(chatClientRequest);
        return callAdvisorChain.nextCall(mutatedRequest);
    }

    private ChatClientRequest augmentPrompt(ChatClientRequest chatClientRequest) {
        Integer maxCharLength = (Integer) chatClientRequest.context().get(MAX_CHAR_LENGTH);
        maxCharLength = maxCharLength == null ? DEFAULT_MAX_CHAR_LENGTH : maxCharLength;

        String userText = "%d자 이내로 답변해 주세요.".formatted(maxCharLength);
        Prompt prompt = chatClientRequest
                .prompt()
                .augmentUserMessage(userMessage -> userMessage
                        .mutate()
                        .text(userMessage.getText() + "\n" + userText)
                        .build());

        return chatClientRequest
                .mutate()
                .prompt(prompt)
                .build();
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
