package chocola.springai.controller.ch13;

import chocola.springai.service.ch13.Exam01WeatherAgent;
import chocola.springai.service.ch13.Exam02WeatherAgent;
import chocola.springai.service.ch13.Exam03WeatherAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("aiController-ch13")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final Exam01WeatherAgent exam01WeatherAgent;
    private final Exam02WeatherAgent exam02WeatherAgent;
    private final Exam03WeatherAgent exam03WeatherAgent;

    @PostMapping("/exam01-weather-agent")
    public String exam01WeatherAgent(@RequestParam String question) {
        return exam01WeatherAgent.execute(question);
    }

    @PostMapping("/exam02-weather-agent")
    public String exam02WeatherAgent(@RequestParam String question) {
        return exam02WeatherAgent.execute(question);
    }

    @PostMapping("/exam03-weather-agent")
    public String exam03WeatherAgent(@RequestParam String question) {
        return exam03WeatherAgent.execute(question);
    }
}
