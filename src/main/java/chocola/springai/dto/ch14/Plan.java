package chocola.springai.dto.ch14;

import java.util.List;
import lombok.Data;

@Data
public class Plan {

    private List<DaySchedule> days;
    private Integer maxBudget;
    private Integer totalCost;
    private Integer meals;
    private Integer accommodation;
    private Integer attractions;
}
