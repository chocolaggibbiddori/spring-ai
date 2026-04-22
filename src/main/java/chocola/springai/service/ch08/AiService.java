package chocola.springai.service.ch08;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.stereotype.Service;

@Slf4j
@Service("aiService-ch08")
@RequiredArgsConstructor
public class AiService {

    private final EmbeddingModel embeddingModel;

    public void textEmbedding(String question) {
        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(question));

        EmbeddingResponseMetadata metadata = embeddingResponse.getMetadata();
        log.info("model: {}", metadata.getModel());
        log.info("dimension: {}", embeddingModel.dimensions());

        float[] vectors = embeddingResponse.getResult().getOutput();
        log.info("vector dimension: {}", vectors.length);
        log.info("vector: {}", vectors);
    }
}
