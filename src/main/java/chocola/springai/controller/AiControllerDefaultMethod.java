package chocola.springai.controller;

import chocola.springai.service.AiServiceDefaultMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerDefaultMethod {

    private final AiServiceDefaultMethod aiServiceDefaultMethod;

    @PostMapping("/default-method")
    public Flux<String> defaultMethod(@RequestParam("question") String question) {
        return aiServiceDefaultMethod.defaultMethod(question);
    }
}
