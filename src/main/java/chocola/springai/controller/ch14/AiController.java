package chocola.springai.controller.ch14;

import chocola.springai.service.ch14.TravelOrchestrator;
import jakarta.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController("aiController-ch14")
@RequiredArgsConstructor
public class AiController {

    private final TravelOrchestrator travelOrchestrator;

    @GetMapping("/chat")
    public SseEmitter chat(@RequestParam("message") String userQuery,
                           HttpSession httpSession) {
        String sessionId = httpSession.getId();

        SseEmitter sseEmitter = new SseEmitter(300000L);

        CompletableFuture.runAsync(() -> {
            try {
                String response = travelOrchestrator.execute(userQuery, sessionId, sseEmitter);
                sendSseEvent(sseEmitter, "message", response);
                sendSseEvent(sseEmitter, "complete", "");
                sseEmitter.complete();
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });

        return sseEmitter;
    }

    private void sendSseEvent(SseEmitter emitter, String event, String data) {
        try {
            emitter.send(SseEmitter.event().name(event).data(data));
        } catch (Exception ignored) {
        }
    }
}
