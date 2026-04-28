package chocola.springai.controller.ch10;

import chocola.springai.service.ch10.ETLService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("/aiController-ch10")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final ETLService etlService;

    @PostMapping("/txt-pdf-docx-etl")
    public String txtPdfDocxEtl(@RequestParam String title,
                                @RequestParam String author,
                                @RequestParam MultipartFile attach) throws IOException {
        return etlService.etlFromFile(title, author, attach);
    }
}
