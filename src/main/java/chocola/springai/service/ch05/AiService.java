package chocola.springai.service.ch05;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service("aiService-ch05")
public class AiService {

    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;

    public AiService(TranscriptionModel transcriptionModel,
                     TextToSpeechModel textToSpeechModel) {
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
}
