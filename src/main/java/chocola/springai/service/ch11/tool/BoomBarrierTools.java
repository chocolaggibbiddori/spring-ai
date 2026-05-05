package chocola.springai.service.ch11.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BoomBarrierTools {

    @Tool(description = "차단기를 올립니다.")
    public String boomBarrierUp() {
        log.info("차단기를 올립니다.");
        return "차단기가 올라갔습니다.";
    }

    @Tool(description = "차단기를 내립니다.", returnDirect = true)
    public String boomBarrierDown() {
        log.info("차단기를 내립니다.");
        return "차단기가 내려갔습니다.";
    }
}
