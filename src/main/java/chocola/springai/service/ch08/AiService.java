package chocola.springai.service.ch08;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service("aiService-ch08")
@RequiredArgsConstructor
public class AiService {

    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public void textEmbedding(String question) {
        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(question));

        EmbeddingResponseMetadata metadata = embeddingResponse.getMetadata();
        log.info("model: {}", metadata.getModel());
        log.info("dimension: {}", embeddingModel.dimensions());

        float[] vectors = embeddingResponse.getResult().getOutput();
        log.info("vector dimension: {}", vectors.length);
        log.info("vector: {}", vectors);
    }

    public void addDocument() {
        List<Document> documentList = List.of(
                new Document("대통령 선거는 5년마다 있습니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("대통령 임기는 4년입니다.", Map.of("source", "헌법", "year", 1980)),
                new Document("국회의원은 법률안을 심의・의결합니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("자동차를 사용하려면 등록을 해야 합니다.", Map.of("source", "자동차관리법")),
                new Document("대통령은 행정부의 수반입니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("국회의원은 4년마다 투표로 뽑습니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("승용차는 정규적인 점검이 필요합니다.", Map.of("source", "자동차관리법"))
        );

        vectorStore.add(documentList);
    }
}
