package chocola.springai.service.ch14.agent;

import chocola.springai.dto.ch14.*;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BudgetAgent {

    public void execute(PlanState state) {
        int maxBudget = state.getMaxBudget();
        Plan plan = state.getPlan();

        calculateAndUpdateCategoryCosts(plan);

        int actualTotalCost = plan.getTotalCost();
        boolean exceeded = actualTotalCost > maxBudget;

        String message = String.format(
                "총 비용: %,d원 | 예산: %,d원 | %s",
                actualTotalCost, maxBudget,
                exceeded ? "⚠️ 초과" : "✅ 정상"
        );

        state.setBudgetAnalysis(new BudgetAnalysis(maxBudget, actualTotalCost, exceeded, message));
    }

    private void calculateAndUpdateCategoryCosts(Plan plan) {
        int mealsCost = 0;
        int accommodationCost = 0;
        int attractionsCost = 0;

        List<DaySchedule> days = plan.getDays();
        if (days != null) {
            for (DaySchedule day : days) {
                List<ScheduleItem> schedule = day.schedule();

                if (schedule != null) {
                    for (ScheduleItem item : schedule) {
                        String type = item.type();
                        int cost = item.cost();

                        switch (type) {
                            case "meal" -> mealsCost += cost;
                            case "accommodation" -> accommodationCost += cost;
                            case "attraction" -> attractionsCost += cost;
                            case null, default -> {
                            }
                        }
                    }
                }
            }
        }

        plan.setMeals(mealsCost);
        plan.setAccommodation(accommodationCost);
        plan.setAttractions(attractionsCost);

        int totalCost = mealsCost + accommodationCost + attractionsCost;
        plan.setTotalCost(totalCost);
    }
}
