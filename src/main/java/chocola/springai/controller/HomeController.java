package chocola.springai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/chat-model")
    public String chatModel() {
        return "chat-model";
    }

    @GetMapping("/chat-model-stream")
    public String chatModelStream() {
        return "chat-model-stream";
    }

    @GetMapping("/prompt-template")
    public String promptTemplate() {
        return "prompt-template";
    }

    @GetMapping("/multi-messages")
    public String multiMessages() {
        return "multi-messages";
    }

    @GetMapping("/default-method")
    public String defaultMethod() {
        return "default-method";
    }

    @GetMapping("/zero-shot-prompt")
    public String zeroShotPrompt() {
        return "zero-shot-prompt";
    }

    @GetMapping("/few-shot-prompt")
    public String fewShotPrompt() {
        return "few-shot-prompt";
    }

    @GetMapping("/role-assignment")
    public String roleAssignment() {
        return "role-assignment";
    }

    @GetMapping("/step-back-prompt")
    public String stepBackPrompt() {
        return "step-back-prompt";
    }

    @GetMapping("/chain-of-thought")
    public String chainOfThought() {
        return "chain-of-thought";
    }

    @GetMapping("/self-consistency")
    public String selfConsistency() {
        return "self-consistency";
    }

    @GetMapping("/list-output-converter")
    public String listOutputConverter() {
        return "list-output-converter";
    }

    @GetMapping("/bean-output-converter")
    public String beanOutputConverter() {
        return "bean-output-converter";
    }

    @GetMapping("/generic-bean-output-converter")
    public String genericBeanOutputConverter() {
        return "generic-bean-output-converter";
    }

    @GetMapping("/map-output-converter")
    public String mapOutputConverter() {
        return "map-output-converter";
    }

    @GetMapping("/system-message")
    public String systemMessage() {
        return "system-message";
    }

    @GetMapping("/stt-tts")
    public String sttTts() {
        return "stt-tts";
    }

    @GetMapping("/stt-llm-tts")
    public String sttLlmTts() {
        return "stt-llm-tts";
    }

    @GetMapping("/chat-voice-stt-llm-tts")
    public String chatVoiceSttLlmTts() {
        return "chat-voice-stt-llm-tts";
    }
}
