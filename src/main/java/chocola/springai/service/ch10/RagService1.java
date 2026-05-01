package chocola.springai.service.ch10;

import java.io.IOException;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SearchRequest.Builder;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.Filter.Expression;
import org.springframework.ai.vectorstore.filter.Filter.ExpressionType;
import org.springframework.ai.vectorstore.filter.Filter.Key;
import org.springframework.ai.vectorstore.filter.Filter.Value;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.Ordered;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RagService1 {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    public RagService1(ChatClient.Builder chatClientBuilder,
                       VectorStore vectorStore,
                       JdbcTemplate jdbcTemplate) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1))
                .build();
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clearVectorStore() {
        jdbcTemplate.update("DELETE FROM chat_memory_vector_store");
    }

    public void ragEtl(MultipartFile attach, String source, int chunkSize, int minChunkSizeChars) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(attach.getBytes());
        PagePdfDocumentReader documentReader = new PagePdfDocumentReader(resource);
        List<Document> documentList = documentReader.read();

        for (Document document : documentList) {
            document.getMetadata().put("source", source);
        }

        TokenTextSplitter transformer = TokenTextSplitter
                .builder()
                .withChunkSize(chunkSize)
                .withMinChunkSizeChars(minChunkSizeChars)
                .build();
        documentList = transformer.transform(documentList);

        vectorStore.add(documentList);
    }

    public String ragChat(String question, double score, String source) {
        Builder builder = SearchRequest
                .builder()
                .similarityThreshold(score)
                .topK(3);

        if (StringUtils.hasText(source)) {
            Expression expression = new Expression(ExpressionType.EQ, new Key("source"), new Value(source));
            builder.filterExpression(expression);
        }

        SearchRequest searchRequest = builder.build();

        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor
                .builder(vectorStore)
                .searchRequest(searchRequest)
                .build();

        return chatClient
                .prompt(question)
                .advisors(questionAnswerAdvisor)
                .call()
                .content();
    }
}
