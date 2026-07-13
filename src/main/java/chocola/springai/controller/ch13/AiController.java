package chocola.springai.controller.ch13;

import chocola.springai.service.ch13.agent.*;
import chocola.springai.service.ch13.dto.Accommodation;
import chocola.springai.service.ch13.dto.Attraction;
import chocola.springai.service.ch13.dto.Restaurant;
import java.util.List;
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
    private final Exam04WeatherAgent exam04WeatherAgent;
    private final Exam05AttractionAgent exam05AttractionAgent;
    private final Exam06RestaurantAgent exam06RestaurantAgent;
    private final Exam07AccommodationAgent exam07AccommodationAgent;

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

    @PostMapping("/exam04-weather-agent")
    public String exam04WeatherAgent(@RequestParam String conversationId, @RequestParam String question) {
        return exam04WeatherAgent.execute(conversationId, question);
    }

    @PostMapping("/exam05-attraction-agent")
    public List<Attraction> exam05AttractionAgent(@RequestParam String question) {
        return exam05AttractionAgent.execute(question);
    }

    @PostMapping("/exam06-restaurant-agent")
    public List<Restaurant> exam06RestaurantAgent(@RequestParam String question) {
        return exam06RestaurantAgent.execute(question);
    }

    @PostMapping("/exam07-accommodation-agent")
    public List<Accommodation> exam07AccommodationAgent(@RequestParam String question) {
        return exam07AccommodationAgent.execute(question);
    }
}
