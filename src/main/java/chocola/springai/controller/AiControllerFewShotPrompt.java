package chocola.springai.controller;

import chocola.springai.service.AiServiceFewShotPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerFewShotPrompt {

    private final AiServiceFewShotPrompt aiServiceFewShotPrompt;

    @PostMapping("/few-shot-prompt")
    public String fewShotPrompt(@RequestParam("order") String order) {
        return aiServiceFewShotPrompt.fewShotPrompt(order);
    }
}
