package chocola.springai.controller;

import chocola.springai.service.AiServiceSelfConsistency;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerSelfConsistency {

    private final AiServiceSelfConsistency aiServiceSelfConsistency;

    @PostMapping("/self-consistency")
    public String selfConsistency(String content) {
        return aiServiceSelfConsistency.selfConsistency(content);
    }
}
