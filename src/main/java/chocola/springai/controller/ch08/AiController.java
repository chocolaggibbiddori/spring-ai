package chocola.springai.controller.ch08;

import static java.util.stream.Collectors.joining;

import chocola.springai.service.ch08.AiService;
import chocola.springai.service.ch08.FaceService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("aiController-ch08")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final FaceService faceService;

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

    @PostMapping("/search-document-1")
    public String searchDocument1(String question) {
        return aiService
                .searchDocument1(question)
                .stream()
                .map(this::makeDocumentHtml)
                .collect(joining("\n"));
    }

    @PostMapping("/search-document-2")
    public String searchDocument2(String question) {
        return aiService
                .searchDocument2(question)
                .stream()
                .map(this::makeDocumentHtml)
                .collect(joining("\n"));
    }

    private String makeDocumentHtml(Document document) {
        Double score = document.getScore();
        String text = document.getText();
        Object year = document.getMetadata().get("year");

        return """
                <div class='mb-2'>
                  <span class='me-2'>유사도 점수: %f,</span>
                  <span>%s(%s)</span>
                </div>
                """.formatted(score, text, year);
    }

    @PostMapping("/delete-document")
    public String deleteDocument() {
        aiService.deleteDocument();
        return "Document가 삭제되었습니다.";
    }

    @PostMapping("/add-face")
    public String addFace(String personName, MultipartFile[] attach) throws IOException {
        for (MultipartFile file : attach) {
            faceService.addFace(personName, file);
        }

        return "얼굴이 저장되었습니다.";
    }

    @PostMapping("/find-face")
    public String findFace(MultipartFile attach) throws IOException {
        return faceService.findFace(attach);
    }
}
