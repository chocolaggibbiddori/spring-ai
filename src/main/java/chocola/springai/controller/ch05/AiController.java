package chocola.springai.controller.ch05;

import chocola.springai.service.ch05.AiService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController("aiController-ch05")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/stt")
    public String stt(@RequestParam("speech") MultipartFile speech) {
        return aiService.stt(speech.getResource());
    }

    @PostMapping("/tts")
    public byte[] tts(@RequestParam("text") String text) {
        return aiService.tts(text);
    }

    @PostMapping("/chat-text")
    public Map<String, String> chatText(@RequestParam("question") String question) {
        return aiService.chatText(question);
    }

    @PostMapping("/chat-voice-stt-llm-tts")
    public StreamingResponseBody chatVoiceSttLlmTts(@RequestParam("question") MultipartFile question) {
        return outputStream -> aiService
                .chatVoiceSttLlmTts(question.getResource())
                .doOnNext(bytes -> writeAndFlush(outputStream, bytes))
                .blockLast();
    }

    private void writeAndFlush(OutputStream outputStream, byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
