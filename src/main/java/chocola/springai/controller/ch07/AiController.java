package chocola.springai.controller.ch07;

import chocola.springai.service.ch07.AiService1;
import chocola.springai.service.ch07.AiService2;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController("/aiController-ch07")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService1 aiService1;
    private final AiService2 aiService2;

    @PostMapping("/advisor-chain")
    public String advisorChain(String question) {
        return aiService1.advisorChain(question);
    }

    @PostMapping("/advisor-chain-stream")
    public Flux<String> advisorChainStream(@RequestBody Map<String, String> requestBody) {
        return aiService1.advisorChainStream(requestBody.get("question"));
    }

    @PostMapping("/advisor-context")
    public String advisorContext(String question) {
        return aiService2.advisorContext(question);
    }
}
