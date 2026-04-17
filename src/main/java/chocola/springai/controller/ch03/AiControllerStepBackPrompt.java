package chocola.springai.controller.ch03;

import chocola.springai.service.ch03.AiServiceStepBackPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerStepBackPrompt {

    private final AiServiceStepBackPrompt aiServiceStepBackPrompt;

    @PostMapping("/step-back-prompt")
    public String stepBackPrompt(@RequestParam("question") String question) throws JsonProcessingException {
        return aiServiceStepBackPrompt.stepBackPrompt(question);
    }
}
