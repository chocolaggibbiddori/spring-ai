package chocola.springai.controller.ch11;

import chocola.springai.service.ch11.*;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("aiController-ch11")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final DateTimeService dateTimeService;
    private final HeatingSystemService heatingSystemService;
    private final RecommendMovieService recommendMovieService;
    private final BoomBarrierService boomBarrierService;
    private final FileSystemService fileSystemService;
    private final InternetSearchService internetSearchService;

    @PostMapping("/date-time-tools")
    public String dateTimeTools(@RequestParam String question) {
        return dateTimeService.chat(question);
    }

    @PostMapping("/heating-system-tools")
    public String heatingSystemTools(@RequestParam String question) {
        return heatingSystemService.chat(question);
    }

    @PostMapping("/recommend-movie-tools")
    public String recommendMovieTools(@RequestParam String question) {
        return recommendMovieService.chat(question);
    }

    @PostMapping("/exception-handling")
    public String exceptionHandling(@RequestParam String question) {
        try {
            return recommendMovieService.chat(question);
        } catch (Exception e) {
            return "[APP] 질문을 처리할 수 없습니다.";
        }
    }

    @PostMapping("/boom-barrier-tools")
    public String boomBarrierTools(@RequestParam MultipartFile attach) throws IOException {
        String contentType = attach.getContentType();
        byte[] bytes = attach.getBytes();

        return boomBarrierService.chat(contentType, bytes);
    }

    @PostMapping("/file-system-tools")
    public String fileSystemTools(@RequestParam String question, HttpSession httpSession) {
        return fileSystemService.chat(question, httpSession.getId());
    }

    @PostMapping("/internet-search-tools")
    public String internetSearchTools(@RequestParam String question) {
        return internetSearchService.chat(question);
    }
}
