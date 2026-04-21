package chocola.springai.controller.ch07;

import chocola.springai.service.ch07.AiService1;
import chocola.springai.service.ch07.AiService2;
import chocola.springai.service.ch07.AiService3;
import chocola.springai.service.ch07.AiService4;
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
    private final AiService3 aiService3;
    private final AiService4 aiService4;

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

    @PostMapping("/advisor-logging")
    public String advisorLogging(String question) {
        return aiService3.advisorLogging(question);
    }

    @PostMapping("/advisor-safe-guard")
    public String advisorSafeGuard(String question) {
        return aiService4.advisorSafeGuard(question);
    }
}
