package chocola.springai.dto.ch14;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleItem(
        String time,
        @JsonProperty(required = true) String type,
        String name,
        String address,
        String description,
        Integer cost
) {
}
