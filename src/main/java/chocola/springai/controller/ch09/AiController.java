package chocola.springai.controller.ch09;

import chocola.springai.service.ch09.AiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("aiController-ch09")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/in-memory-chat")
    public String inMemoryChatMemory(String question, HttpSession session) {
        return aiService.chatInMemory(question, session.getId());
    }
}
