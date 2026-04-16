package chocola.springai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerPromptTemplate {

    private final AiServicePromptTemplate aiServicePromptTemplate;

    @PostMapping("/prompt-template")
    public Flux<String> promptTemplate(@RequestParam("statement") String statement,
                                       @RequestParam("language") String language) {
//        return aiServicePromptTemplate.promptTemplate1(statement, language);
//        return aiServicePromptTemplate.promptTemplate2(statement, language);
        return aiServicePromptTemplate.promptTemplate3(statement, language);
    }
}
