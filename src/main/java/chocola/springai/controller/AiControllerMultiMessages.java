package chocola.springai.controller;

import chocola.springai.service.AiServiceMultiMessage;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerMultiMessages {

    private final AiServiceMultiMessage aiServiceMultiMessage;

    @PostMapping("/multi-messages")
    public String multiMessage(@RequestParam("question") String question,
                               HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Message> chatMemory = (List<Message>) session.getAttribute("chatMemory");

        if (chatMemory == null) {
            session.setAttribute("chatMemory", chatMemory = new ArrayList<>());
        }

        return aiServiceMultiMessage.multiMessage(question, chatMemory);
    }
}
