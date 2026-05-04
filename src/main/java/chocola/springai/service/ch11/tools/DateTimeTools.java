package chocola.springai.service.ch11.tools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DateTimeTools {

    @Tool(description = "현재 날짜와 시간 정보를 제공합니다.")
    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();

        log.info("현재 시간: {}", now);

        return now.toString();
    }

    @Tool(description = "지정된 시간에 알람을 설정합니다.")
    public void setAlarm(@ToolParam(description = "ISO-8601 형식의 시간 문자열") String time) {
        if (time.contains("T24:")) {
            int tIndex = time.indexOf("T");
            String datePart = time.substring(0, tIndex);
            String timePart = time.substring(tIndex + 1);

            LocalDate date = LocalDate.parse(datePart);
            date = date.plusDays(1L);

            timePart = timePart.replaceFirst("24", "00");
            time = date + "T" + timePart;
        }

        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);

        log.info("알람 설정 시간: {}", alarmTime);
    }
}
