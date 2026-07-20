package chocola.springai.dto.ch14;

import java.util.List;

public record DaySchedule(
        int day,
        List<ScheduleItem> schedule) {
}
