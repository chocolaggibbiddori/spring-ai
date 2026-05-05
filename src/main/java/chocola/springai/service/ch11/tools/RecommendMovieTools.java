package chocola.springai.service.ch11.tools;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecommendMovieTools {

    @Tool(description = "사용자가 관람한 영화 목록을 제공합니다.")
    public List<String> getMovieListByUserId(@ToolParam(description = "사용자 ID") String userId) {
        throw new RuntimeException("사용자 ID가 존재하지 않습니다.");
    }

    @Tool(description = "주어진 장르의 추천 영화 목록을 제공합니다.", returnDirect = true)
    public List<String> recommendMovie(@ToolParam(description = "추천할 영화 장르") String genre) {
        log.info("Recommend movie for genre: {}", genre);
        return List.of("크레이븐", "베놈", "메이드");
    }
}
