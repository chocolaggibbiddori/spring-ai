package chocola.springai.controller.ch08;

import chocola.springai.service.ch08.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("aiController-ch08")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/text-embedding")
    public String textEmbedding(String question) {
        aiService.textEmbedding(question);
        return "서버 터미널 출력을 확인하세요.";
    }

    @PostMapping("/add-document")
    public String addDocument() {
        aiService.addDocument();
        return "벡터 저장소에 Document가 저장되었습니다.";
    }
}
