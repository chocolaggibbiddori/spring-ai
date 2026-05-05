package chocola.springai.service.ch11.tools;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CarCheckTools {

    private final List<String> carNumberList = List.of("23가4567", "234부8372", "346서5850");

    @Tool(description = "인식된 차량 번호가 등록되어 있는 지 확인합니다.")
    public boolean checkCarNumber(@ToolParam(description = "차량 번호") String carNumber) {
        carNumber = carNumber.replaceAll("\\s+", "");

        log.info("LLM이 인식한 차량 번호: {}", carNumber);

        return carNumberList.contains(carNumber);
    }
}
