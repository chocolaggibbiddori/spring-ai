package chocola.springai.controller.ch04;

import chocola.springai.service.ch04.AiServiceListOutputConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerListOutputConverter {

    private final AiServiceListOutputConverter aiServiceListOutputConverter;

    @PostMapping("/list-output-converter")
    public List<String> listOutputConverter(String city) {
//        return aiServiceListOutputConverter.listOutputConverterLowLevel(city);
        return aiServiceListOutputConverter.listOutputConverterHighLevel(city);
    }
}
