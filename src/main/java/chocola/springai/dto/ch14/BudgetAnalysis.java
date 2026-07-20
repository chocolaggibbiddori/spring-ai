package chocola.springai.dto.ch14;

public record BudgetAnalysis(
        Integer maxBudget,
        Integer totalCost,
        boolean exceeded,
        String message) {
}
