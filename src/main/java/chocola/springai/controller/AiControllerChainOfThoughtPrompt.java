package chocola.springai.controller;

import chocola.springai.service.AiServiceChainOfThoughtPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerChainOfThoughtPrompt {

    private final AiServiceChainOfThoughtPrompt aiService;

    @PostMapping("/chain-of-thought")
    public Flux<String> chainOfThought(String question) {
        return aiService.chainOfThought(question);
    }
}
