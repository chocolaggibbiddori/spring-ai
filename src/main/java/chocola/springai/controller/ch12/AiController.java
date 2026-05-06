package chocola.springai.controller.ch12;

import chocola.springai.service.ch12.AiService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("aiController-ch12")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/mcp-chat")
    public String mcpChat(@RequestParam String question) {
        return aiService.chat(question);
    }

    @PostMapping("/mcp-boom-barrier")
    public String mcpBoomBarrier(@RequestParam MultipartFile attach) throws IOException {
        String contentType = attach.getContentType();
        byte[] bytes = attach.getBytes();

        return aiService.boomBarrier(contentType, bytes);
    }
}
