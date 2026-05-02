package chocola.springai.controller.ch10;

import chocola.springai.service.ch10.ETLService;
import chocola.springai.service.ch10.RagService1;
import chocola.springai.service.ch10.RagService2;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController("/aiController-ch10")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final ETLService etlService;
    private final RagService1 ragService1;
    private final RagService2 ragService2;

    @PostMapping("/txt-pdf-docx-etl")
    public String txtPdfDocxEtl(@RequestParam String title,
                                @RequestParam String author,
                                @RequestParam MultipartFile attach) throws IOException {
        return etlService.etlFromFile(title, author, attach);
    }

    @PostMapping("/html-etl")
    public String htmlEtl(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam String url) throws IOException {
        return etlService.etlFromHtml(title, author, url);
    }

    @PostMapping("/json-etl")
    public String jsonEtl(@RequestParam String url) throws IOException {
        return etlService.etlFromJson(url);
    }

    @GetMapping("/rag-clear")
    public String ragClear() {
        ragService1.clearVectorStore();
        return "벡터 저장소의 데이터를 모두 삭제했습니다.";
    }

    @PostMapping("/rag-etl")
    public String ragEtl(@RequestParam MultipartFile attach,
                         @RequestParam String source,
                         @RequestParam(defaultValue = "200") int chunkSize,
                         @RequestParam(defaultValue = "100") int minChunkSizeChars) throws IOException {
        ragService1.ragEtl(attach, source, chunkSize, minChunkSizeChars);
        return "PDF ETL 과정을 성공적으로 처리했습니다.";
    }

    @PostMapping("/rag-chat")
    public String ragChat(@RequestParam String question,
                          @RequestParam(defaultValue = "0.0") double score,
                          @RequestParam String source) {
        return ragService1.ragChat(question, score, source);
    }

    @PostMapping("/compression-query-transformer")
    public String compressionQueryTransformer(@RequestParam String question,
                                              @RequestParam(defaultValue = "0.0") double score,
                                              @RequestParam String source,
                                              HttpSession httpSession) {
        return ragService2.chatWithCompression(question, score, source, httpSession.getId());
    }

    @PostMapping("/rewrite-query-transformer")
    public String rewriteQueryTransformer(@RequestParam String question,
                                          @RequestParam(defaultValue = "0.0") double score,
                                          @RequestParam String source) {
        return ragService2.chatWithRewriteQuery(question, score, source);
    }

    @PostMapping("/translation-query-transformer")
    public String translationQueryTransformer(@RequestParam String question,
                                              @RequestParam(defaultValue = "0.0") double score,
                                              @RequestParam String source) {
        return ragService2.chatWithTranslation(question, score, source);
    }
}
