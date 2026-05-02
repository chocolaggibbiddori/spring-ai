package chocola.springai.service.ch10;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter.Expression;
import org.springframework.ai.vectorstore.filter.Filter.ExpressionType;
import org.springframework.ai.vectorstore.filter.Filter.Key;
import org.springframework.ai.vectorstore.filter.Filter.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RagService2 {

    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    public RagService2(ChatClient.Builder chatClientBuilder,
                       ChatModel chatModel,
                       VectorStore vectorStore,
                       ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1))
                .build();
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

    private CompressionQueryTransformer createCompressionQueryTransformer() {
        Builder builder = ChatClient
                .builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1));

        return new CompressionQueryTransformer(builder, null);
    }

    private VectorStoreDocumentRetriever createVectorStoreDocumentRetriever(double score, String source) {
        return new VectorStoreDocumentRetriever(vectorStore, score, 3,
                () -> StringUtils.hasText(source)
                        ? new Expression(ExpressionType.EQ, new Key("source"), new Value(source))
                        : null);
    }

    public String chatWithCompression(String question, double score, String source, String conversationId) {
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build();

        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor
                .builder()
                .queryTransformers(createCompressionQueryTransformer())
                .documentRetriever(createVectorStoreDocumentRetriever(score, source))
                .build();

        return chatClient
                .prompt(question)
                .advisors(messageChatMemoryAdvisor, retrievalAugmentationAdvisor)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    private RewriteQueryTransformer createRewriteQueryTransformer() {
        Builder builder = ChatClient
                .builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1));

        return new RewriteQueryTransformer(builder, null, null);
    }

    public String chatWithRewriteQuery(String question, double score, String source) {
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor
                .builder()
                .queryTransformers(createRewriteQueryTransformer())
                .documentRetriever(createVectorStoreDocumentRetriever(score, source))
                .build();

        return chatClient
                .prompt(question)
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .content();
    }
}
