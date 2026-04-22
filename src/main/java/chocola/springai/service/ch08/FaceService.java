package chocola.springai.service.ch08;

import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class FaceService {

    private final JdbcTemplate jdbcTemplate;
    private final WebClient webClient;

    public FaceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.webClient = WebClient.builder().build();
    }

    public void addFace(String personName, MultipartFile file) throws IOException {
        float[] vector = getFaceVector(file);
        String vectorString = Arrays.toString(vector).replace(" ", "");
        String sql = """
                INSERT
                INTO face_vector_store (person_name, embedding)
                VALUES (
                  ?,
                  ?::VECTOR
                )
                """;

        jdbcTemplate.update(sql, personName, vectorString);
    }

    public String findFace(MultipartFile file) throws IOException {
        float[] vector = getFaceVector(file);
        String vectorString = Arrays.toString(vector).replace(" ", "");
        String sql = """
                SELECT
                  person_name,
                  embedding <=> ?::VECTOR AS similarity
                FROM
                  face_vector_store
                ORDER BY
                  embedding <=> ?::VECTOR
                LIMIT 3
                """;

        return jdbcTemplate
                .queryForList(sql, vectorString, vectorString)
                .stream()
                .peek(row -> log.info("{} (코사인: {})", row.get("person_name"), row.get("similarity")))
                .findFirst()
                .filter(row -> (double) row.get("similarity") <= 0.3)
                .map(row -> row.get("person_name").toString())
                .orElse("등록된 사람이 아닙니다.");
    }

    private float[] getFaceVector(MultipartFile file) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder
                .part("file", file.getBytes())
                .filename(file.getOriginalFilename())
                .contentType(MediaType.valueOf(file.getContentType()));
        MultiValueMap<String, HttpEntity<?>> multipartForm = builder.build();

        return webClient
                .post()
                .uri("http://localhost:50001/get-face-vector")
                .bodyValue(multipartForm)
                .retrieve()
                .bodyToMono(FaceEmbedApiResponse.class)
                .block()
                .vector();
    }

    public record FaceEmbedApiResponse(float[] vector) {
    }
}
