package chocola.springai.controller.ch04;

import chocola.springai.dto.ch04.Hotel;
import chocola.springai.service.ch04.AiServiceBeanOutputConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerBeanOutputConverter {

    private final AiServiceBeanOutputConverter aiServiceBeanOutputConverter;

    @PostMapping("/bean-output-converter")
    public Hotel beanOutputConverter(String city) {
        return aiServiceBeanOutputConverter.beanOutputConverterHighLevel(city);
    }
}
