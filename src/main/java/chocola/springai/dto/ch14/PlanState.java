package chocola.springai.dto.ch14;

import chocola.springai.service.ch13.dto.Accommodation;
import chocola.springai.service.ch13.dto.Attraction;
import chocola.springai.service.ch13.dto.Restaurant;
import java.util.List;
import lombok.Data;

@Data
public class PlanState {

    private String userQuery;
    private String destination;
    private Integer days;
    private Integer maxBudget;
    private List<Attraction> attractions;
    private List<Restaurant> restaurants;
    private List<Accommodation> accommodations;
    private BudgetAnalysis budgetAnalysis;
    private Plan plan;
    private boolean replan;
    private Integer previousTotalCost;
}
