package chocola.springai.controller.ch12;

import chocola.springai.service.ch12.AiService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController("aiController-ch12")
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/mcp-chat")
    public Flux<String> mcpChat(@RequestBody Map<String, String> map) {
        return aiService.chat(map.get("question"));
    }

    @PostMapping("/mcp-boom-barrier")
    public Flux<String> mcpBoomBarrier(@RequestPart FilePart attach) {
        String contentType = attach.headers().getContentType().toString();
        Flux<DataBuffer> content = attach.content();

        return DataBufferUtils
                .join(content)
                .flatMapMany(buffer -> {
                    try {
                        int size = buffer.readableByteCount();
                        byte[] bytes = new byte[size];
                        buffer.read(bytes);

                        return aiService.boomBarrier(contentType, bytes);
                    } finally {
                        DataBufferUtils.release(buffer);
                    }
                });
    }
}
