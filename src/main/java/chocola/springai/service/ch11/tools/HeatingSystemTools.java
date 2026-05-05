package chocola.springai.service.ch11.tools;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeatingSystemTools {

    @Tool(description = """
            타겟 온도까지 난방 시스템을 가동합니다.
            난방 시스템 가동이 성공했을 경우 success를 반환합니다.
            난방 시스템 가동이 실패했을 경우 failure를 반환합니다.
            """)
    public String startHeatingSystem(@ToolParam(description = "타겟 온도") int targetTemperature,
                                     ToolContext toolContext) {
        Object controlKeyObj = toolContext.getContext().get("controlKey");

        if (controlKeyObj instanceof String controlKey && controlKey.equals("heatingSystemKey")) {
            log.info("{}도까지 난방 시스템을 가동합니다.", targetTemperature);
            return "success";
        }

        log.info("난방 시스템을 가동할 권한이 없습니다.");
        return "failure";
    }

    @Tool(description = """
            난방 시스템을 중지합니다.
            난방 시스템 중지가 성공했을 경우 success를 반환합니다.
            난방 시스템 중지가 실패했을 경우 failure를 반환합니다.
            """)
    public String stopHeatingSystem(ToolContext toolContext) {
        Object controlKeyObj = toolContext.getContext().get("controlKey");

        if (controlKeyObj instanceof String controlKey && controlKey.equals("heatingSystemKey")) {
            log.info("난방 시스템을 중지합니다.");
            return "success";
        }

        log.info("난방 시스템을 중지할 권한이 없습니다.");
        return "failure";
    }

    @Tool(description = "현재 온도를 제공합니다.")
    public int getTemperature() {
        int temperature = new Random().nextInt(13, 30);

        log.info("현재 온도: {}", temperature);

        return temperature;
    }
}
