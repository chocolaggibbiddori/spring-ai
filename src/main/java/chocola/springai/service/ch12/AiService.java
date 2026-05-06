package chocola.springai.service.ch12;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service("aiService-ch12")
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = builder
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    public String chat(String question) {
        return chatClient
                .prompt(question)
                .system("""
                        현재 날짜와 시간 질문은 반드시 도구를 사용하세요.
                        파일과 디렉토리 관련 질문은 반드시 도구를 사용하세요.
                        """)
                .call()
                .content();
    }

    public String boomBarrier(String contentType, byte[] bytes) {
        Media media = Media
                .builder()
                .mimeType(MediaType.valueOf(contentType))
                .data(new ByteArrayResource(bytes))
                .build();

        UserMessage userMessage = UserMessage
                .builder()
                .media(media)
                .text("""
                        다음 단계별로 처리해 주세요.
                        1단계: 이미지에서 '(숫자 2~3개)-(한글 1자)-(숫자 4개)'로 구성된 차량 번호를 인식하세요. 예: 78라1234, 567바2558
                        2단계: 인식된 차량 번호에서 끝에서부터 5번째 문자가 한글 완성형 음절이 아닐 경우에는 다시 1단계로 돌아가세요.
                        3단계: 1단계에서 인식된 차량 번호가 등록된 차량 번호인지 도구로 확인을 하세요.
                        4단계: 3단계의 결과가 false라면 도구로 차단기를 내리고, true라면 도구로 차단기를 올리세요.
                        
                        최종 답변은 '차단기 내림' 또는 '차단기 올림'으로 하고 추가 설명은 하지 마세요.
                        """)
                .build();

        return chatClient
                .prompt()
                .messages(userMessage)
                .call()
                .content();
    }
}
