package chocola.springai.service.ch05;

import java.util.Base64;
import java.util.Map;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.Speech;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service("aiService-ch05")
public class AiService {

    private final ChatClient chatClient;
    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;

    public AiService(ChatClient.Builder chatClientBuilder,
                     TranscriptionModel transcriptionModel,
                     TextToSpeechModel textToSpeechModel) {
        this.chatClient = chatClientBuilder
                .defaultSystem("50자 이내로 답변해 주세요.")
                .build();
        this.transcriptionModel = transcriptionModel;
        this.textToSpeechModel = textToSpeechModel;
    }

    public String stt(Resource resource) {
        return transcriptionModel
                .call(new AudioTranscriptionPrompt(resource))
                .getResult()
                .getOutput();
    }

    public byte[] tts(String text) {
        return textToSpeechModel
                .call(new TextToSpeechPrompt(text))
                .getResult()
                .getOutput();
    }

    public Map<String, String> chatText(String question) {
        String answer = chatClient
                .prompt(question)
                .call()
                .content();

        if (answer == null) {
            return Map.of();
        }

        byte[] audioBytes = tts(answer);
        String encodedAudio = Base64.getEncoder().encodeToString(audioBytes);

        return Map.of("text", answer, "audio", encodedAudio);
    }

    public Flux<byte[]> ttsFlux(String text) {
        return textToSpeechModel
                .stream(new TextToSpeechPrompt(text))
                .map(TextToSpeechResponse::getResult)
                .map(Speech::getOutput);
    }

    public Flux<byte[]> chatVoiceSttLlmTts(Resource resource) {
        String question = stt(resource);
        String answer = chatClient
                .prompt(question)
                .options(ChatOptions
                        .builder()
                        .maxTokens(100)
                        .build())
                .call()
                .content();

        return ttsFlux(answer);
    }
}
