package chocola.springai.controller.ch11;

import chocola.springai.service.ch11.DateTimeService;
import chocola.springai.service.ch11.HeatingSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("aiController-ch11")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final DateTimeService dateTimeService;
    private final HeatingSystemService heatingSystemService;

    @PostMapping("/date-time-tools")
    public String dateTimeTools(@RequestParam String question) {
        return dateTimeService.chat(question);
    }

    @PostMapping("/heating-system-tools")
    public String heatingSystemTools(@RequestParam String question) {
        return heatingSystemService.chat(question);
    }
}
