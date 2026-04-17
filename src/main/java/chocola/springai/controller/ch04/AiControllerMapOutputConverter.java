package chocola.springai.controller.ch04;

import chocola.springai.service.ch04.AiServiceMapOutputConverter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerMapOutputConverter {

    private final AiServiceMapOutputConverter aiServiceMapOutputConverter;

    @PostMapping("/map-output-converter")
    public Map<String, Object> mapOutputConverter(String hotel) {
        return aiServiceMapOutputConverter.mapOutputConverterHighLevel(hotel);
    }
}
