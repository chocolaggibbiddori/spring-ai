package chocola.springai.service.ch10;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ETLService {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public String etlFromFile(String title, String author, MultipartFile attach) throws IOException {
        List<Document> documentList = extractFromFile(attach);
        if (documentList == null) {
            return ".txt, .pdf, .doc, .docx 파일 중에 하나를 올려주세요.";
        }

        log.info("추출된 Document 수: {} 개", documentList.size());

        for (Document document : documentList) {
            Map<String, Object> metadata = document.getMetadata();
            metadata.putAll(
                    Map.of("title", title,
                            "author", author,
                            "source", Objects.requireNonNull(attach.getOriginalFilename())));
        }

        documentList = transform(documentList);
        log.info("변환된 Document 수: {} 개", documentList.size());

        vectorStore.add(documentList);

        return "올린 문서를 추출-변환-적재 완료했습니다.";
    }

    private List<Document> extractFromFile(MultipartFile attach) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(attach.getBytes());
        String contentType = Objects.requireNonNull(attach.getContentType());

        return switch (contentType) {
            case "text/plain" -> new TextReader(resource).read();
            case "application/pdf" -> new PagePdfDocumentReader(resource).read();
            case "wordprocessingml" -> new TikaDocumentReader(resource).read();
            default -> null;
        };
    }

    private List<Document> transform(List<Document> documentList) {
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        documentList = tokenTextSplitter.split(documentList);

        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 5);
        return keywordMetadataEnricher.transform(documentList);
    }
}
