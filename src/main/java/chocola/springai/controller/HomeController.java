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
        return "ch02/chat-model";
    }

    @GetMapping("/chat-model-stream")
    public String chatModelStream() {
        return "ch02/chat-model-stream";
    }

    @GetMapping("/prompt-template")
    public String promptTemplate() {
        return "ch03/prompt-template";
    }

    @GetMapping("/multi-messages")
    public String multiMessages() {
        return "ch03/multi-messages";
    }

    @GetMapping("/default-method")
    public String defaultMethod() {
        return "ch03/default-method";
    }

    @GetMapping("/zero-shot-prompt")
    public String zeroShotPrompt() {
        return "ch03/zero-shot-prompt";
    }

    @GetMapping("/few-shot-prompt")
    public String fewShotPrompt() {
        return "ch03/few-shot-prompt";
    }

    @GetMapping("/role-assignment")
    public String roleAssignment() {
        return "ch03/role-assignment";
    }

    @GetMapping("/step-back-prompt")
    public String stepBackPrompt() {
        return "ch03/step-back-prompt";
    }

    @GetMapping("/chain-of-thought")
    public String chainOfThought() {
        return "ch03/chain-of-thought";
    }

    @GetMapping("/self-consistency")
    public String selfConsistency() {
        return "ch03/self-consistency";
    }

    @GetMapping("/list-output-converter")
    public String listOutputConverter() {
        return "ch04/list-output-converter";
    }

    @GetMapping("/bean-output-converter")
    public String beanOutputConverter() {
        return "ch04/bean-output-converter";
    }

    @GetMapping("/generic-bean-output-converter")
    public String genericBeanOutputConverter() {
        return "ch04/generic-bean-output-converter";
    }

    @GetMapping("/map-output-converter")
    public String mapOutputConverter() {
        return "ch04/map-output-converter";
    }

    @GetMapping("/system-message")
    public String systemMessage() {
        return "ch04/system-message";
    }

    @GetMapping("/stt-tts")
    public String sttTts() {
        return "ch05/stt-tts";
    }

    @GetMapping("/stt-llm-tts")
    public String sttLlmTts() {
        return "ch05/stt-llm-tts";
    }

    @GetMapping("/chat-voice-stt-llm-tts")
    public String chatVoiceSttLlmTts() {
        return "ch05/chat-voice-stt-llm-tts";
    }

    @GetMapping("/chat-voice-one-model")
    public String chatVoiceOneModel() {
        return "ch05/chat-voice-one-model";
    }

    @GetMapping("/image-analysis")
    public String imageAnalysis() {
        return "ch06/image-analysis";
    }

    @GetMapping("/video-analysis")
    public String videoAnalysis() {
        return "ch06/video-analysis";
    }

    @GetMapping("/image-generation")
    public String imageGeneration() {
        return "ch06/image-generation";
    }

    @GetMapping("/advisor-chain")
    public String advisorChain() {
        return "ch07/advisor-chain";
    }

    @GetMapping("/advisor-chain-stream")
    public String advisorChainStream() {
        return "ch07/advisor-chain-stream";
    }

    @GetMapping("/advisor-context")
    public String advisorContext() {
        return "ch07/advisor-context";
    }

    @GetMapping("/advisor-logging")
    public String advisorLogging() {
        return "ch07/advisor-logging";
    }

    @GetMapping("/advisor-safe-guard")
    public String advisorSafeGuard() {
        return "ch07/advisor-safe-guard";
    }

    @GetMapping("/text-embedding")
    public String textEmbedding() {
        return "ch08/text-embedding";
    }

    @GetMapping("/add-document")
    public String addDocument() {
        return "ch08/add-document";
    }

    @GetMapping("/search-document-1")
    public String searchDocument1() {
        return "ch08/search-document-1";
    }

    @GetMapping("/search-document-2")
    public String searchDocument2() {
        return "ch08/search-document-2";
    }

    @GetMapping("/delete-document")
    public String deleteDocument() {
        return "ch08/delete-document";
    }

    @GetMapping("/image-embedding")
    public String imageEmbedding() {
        return "ch08/image-embedding";
    }

    @GetMapping("/txt-pdf-word-etl")
    public String txtPdfWordEtl() {
        return "ch09/txt-pdf-word-etl";
    }
}
