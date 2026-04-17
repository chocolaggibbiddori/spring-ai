package chocola.springai.controller.ch03;

import chocola.springai.service.ch03.AiServiceZeroShotPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerZeroShotPrompt {

    private final AiServiceZeroShotPrompt aiServiceZeroShotPrompt;

    @PostMapping("/zero-shot-prompt")
    public String zeroShotPrompt(@RequestParam("review") String review) {
        return aiServiceZeroShotPrompt.zeroShotPrompt(review);
    }
}
