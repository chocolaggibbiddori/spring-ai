package chocola.springai.controller.ch04;

import chocola.springai.dto.ch04.ReviewClassification;
import chocola.springai.service.ch04.AiServiceSystemMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerSystemMessage {

    private final AiServiceSystemMessage aiServiceSystemMessage;

    @PostMapping("/system-message")
    public ReviewClassification systemMessage(String review) {
        return aiServiceSystemMessage.classifyReview(review);
    }
}
