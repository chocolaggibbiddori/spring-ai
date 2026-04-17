package chocola.springai.controller.ch04;

import chocola.springai.dto.ch04.Hotel;
import chocola.springai.service.ch04.AiServiceParameterizedTypeReference;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerParameterizedTypeReference {

    private final AiServiceParameterizedTypeReference aiServiceParameterizedTypeReference;

    @PostMapping("/generic-bean-output-converter")
    public List<Hotel> genericBeanOutputConverter(String cities) {
        return aiServiceParameterizedTypeReference.genericBeanOutputConverterHighLevel(cities);
    }
}
