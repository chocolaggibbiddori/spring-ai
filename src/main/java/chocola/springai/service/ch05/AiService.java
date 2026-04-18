package chocola.springai.service.ch05;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.Speech;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters.Voice;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

@Slf4j
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

    public byte[] chatVoiceOneModel(Resource resource, String mimeType) {
        UserMessage userMessage = UserMessage
                .builder()
                .text("제공되는 음성에 맞는 자연스러운 대화로 이어주세요.")
                .media(new Media(MimeType.valueOf(mimeType), resource))
                .build();

        ChatOptions chatOptions = OpenAiChatOptions
                .builder()
                .model(ChatModel.GPT_4_O_MINI_AUDIO_PREVIEW)
                .outputModalities(List.of("text", "audio"))
                .outputAudio(new AudioParameters(Voice.ALLOY, AudioResponseFormat.MP3))
                .build();

        AssistantMessage assistantMessage = chatClient
                .prompt()
                .messages(userMessage)
                .options(chatOptions)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();

        String answer = assistantMessage.getText();
        log.info("텍스트 응답: {}", answer);

        return assistantMessage
                .getMedia()
                .getFirst()
                .getDataAsByteArray();
    }
}
