package chocola.springai.service.ch11.tool;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class InternetSearchTools {

    private final String apiKey;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public InternetSearchTools(@Value("${serpapi.endpoint}") String searchEndpoint,
                               @Value("${serpapi.api-key}") String apiKey,
                               RestClient.Builder restClientBuilder) {
        this.apiKey = apiKey;
        this.restClient = restClientBuilder
                .baseUrl(searchEndpoint)
                .defaultHeader("Accept", "application/json")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "인터넷 검색을 합니다. 제목, 링크, 요약을 문자열로 반환합니다.")
    public String search(String query) {
        String body = restClient
                .get()
                .uri(builder -> builder
                        .queryParam("engine", "google")
                        .queryParam("q", query)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .body(String.class);

        log.info("검색 결과: {}", body);

        JsonNode root;
        try {
            root = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            return "검색 결과를 파싱하는 중 오류가 발생했습니다.";
        }

        JsonNode organicResults = root.path("organic_results");

        if (!organicResults.isArray() || organicResults.isEmpty()) {
            return "검색 결과가 없습니다.";
        }

        String result = organicResults
                .valueStream()
                .limit(3L)
                .map(node -> "%d. %s\n%s\n%s\n".formatted(
                        node.path("position").asInt(),
                        node.path("title").asText(),
                        node.path("link").asText(),
                        node.path("snippet").asText()))
                .collect(joining("\n"));

        log.info(result);

        return result;
    }

    @Tool(description = "웹 페이지의 본문 텍스트를 반환합니다.")
    public String fetch(String url) {
        String html = restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        if (html == null || html.isBlank()) {
            return "페이지 내용을 가져올 수 없습니다.";
        }

        Document document = Jsoup.parse(html);
        String text = document.body().text();

        return text.isBlank() ? "페이지 내용이 비어 있습니다." : text;
    }
}
