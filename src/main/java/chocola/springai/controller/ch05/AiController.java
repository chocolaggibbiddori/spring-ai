package chocola.springai.controller.ch05;

import chocola.springai.service.ch05.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
