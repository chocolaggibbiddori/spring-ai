package chocola.springai.controller;

import chocola.springai.service.AiServiceByChatClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

//    private final AiService aiService;
    private final AiServiceByChatClient aiService;

    @PostMapping("/chat")
    public String chat(@RequestParam("question") String question) {
        return "아직 모델과 연결되지 않았습니다.";
    }

    @PostMapping("/chat-model")
    public String chatModel(@RequestParam("question") String question) {
        return aiService.generateText(question);
    }

    @PostMapping("/chat-model-stream")
    public Flux<String> chatModelStream(@RequestParam("question") String question) {
        return aiService.generateTextStream(question);
    }
}
