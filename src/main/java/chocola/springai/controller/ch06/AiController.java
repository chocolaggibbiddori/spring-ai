package chocola.springai.controller.ch06;

import chocola.springai.service.ch06.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController("aiController-ch06")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/image-analysis")
    public Flux<String> imageAnalysis(@RequestParam("question") String question,
                                      @RequestParam(value = "attach", required = false) MultipartFile attach) {
        if (attach == null) {
            return Flux.just("이미지를 올려주세요.");
        }

        String contentType = attach.getContentType();
        if (contentType == null || !contentType.contains("image/")) {
            return Flux.just("이미지를 올려주세요.");
        }

        Resource resource = attach.getResource();
        return aiService.imageAnalysis(question, contentType, resource);
    }

    @PostMapping("/image-generate")
    public String imageGenerate(@RequestParam("description") String description) {
        return aiService.generateImage(description);
    }
}
