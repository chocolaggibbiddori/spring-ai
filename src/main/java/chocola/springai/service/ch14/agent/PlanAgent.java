package chocola.springai.service.ch14.agent;

import chocola.springai.dto.ch14.Plan;
import chocola.springai.dto.ch14.PlanState;
import chocola.springai.service.ch13.dto.Accommodation;
import chocola.springai.service.ch13.dto.Attraction;
import chocola.springai.service.ch13.dto.Restaurant;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class PlanAgent {

    private static final String SYSTEM_PROMPT = """
            당신은 여행 일정을 계획하는 전문 에이전트입니다.
            주어진 여행 정보와 장소 목록을 바탕으로 실용적이고 균형잡힌 여행 일정을 작성합니다.
            예산을 고려하면서도 여행자가 충분히 즐길 수 있도록 다양한 장소를 선택합니다.
            """;

    private static final String USER_PROMPT_TEMPLATE = """
            제주도 {days}일 여행 일정을 작성해주세요.
            
            ## 여행 정보
            - 여행 기간: {days}일
            - 총 예산: {budget}원
            
            ## 추천 관광지 목록
            {attractions}
            
            ## 추천 맛집 목록
            {restaurants}
            
            ## 추천 숙소 목록
            {accommodations}
            
            ## 일정 작성 규칙
            1. 매일 포함할 항목:
               - 오전 관광지 1-2곳 (09:00-12:00)
               - 점심 식사 (12:00-13:00) - 맛집에서 선택
               - 오후 관광지 1-2곳 (14:00-18:00)
               - 저녁 식사 (18:00-19:00) - 맛집에서 선택
               - 숙소 체크인 (20:00) - 마지막 날 제외
            
            2. 숙박 규칙:
               - {days}일 여행 = {nights}박
               - 마지막 날에는 숙소가 필요 없음
            
            3. 중복 방지 규칙 (매우 중요):
               - 같은 관광지는 전체 일정에서 단 1번만 방문
               - 같은 맛집은 전체 일정에서 단 1번만 방문 (점심과 저녁에 다른 맛집 선택)
               - 같은 숙소는 전체 일정에서 단 1번만 사용
               - 각 날짜의 점심과 저녁은 반드시 다른 맛집을 선택
            
            4. 일정 작성 형식:
               - 각 일정 항목에 반드시 포함: time, type, name, address, description, cost
               - type은 정확히: 'attraction', 'meal', 'accommodation'
               - time은 HH:mm 형식 (예: 09:00, 12:30)
               - name: 위 목록의 이름을 정확히 사용
               - address: 위 목록의 주소를 그대로 복사
               - description: 위 목록의 설명을 그대로 복사
               - cost: 위 목록의 비용을 그대로 사용 (숫자만, 원 단위 생략)
               - 식사는 '점심 - 식당이름' 또는 '저녁 - 식당이름' 형식으로 작성
            """;

    private final ChatClient chatClient;

    public PlanAgent(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    public void execute(PlanState state) {
        String prompt = buildTravelPlanPrompt(state);

        Plan plan = chatClient
                .prompt(prompt)
                .call()
                .entity(Plan.class);

        state.setPlan(plan);
    }

    private String buildTravelPlanPrompt(PlanState state) {
        StringBuilder attractions = new StringBuilder();
        List<Attraction> attractionList = state.getAttractions();
        if (attractionList != null) {
            for (Attraction attr : attractionList) {
                attractions
                        .append("- ")
                        .append(attr.name())
                        .append(" (입장료: ")
                        .append(String.format("%,d", attr.entranceFee()))
                        .append("원)\n")
                        .append("  위치: ")
                        .append(attr.address())
                        .append("\n")
                        .append("  설명: ")
                        .append(attr.description())
                        .append("\n");
            }
        }

        StringBuilder restaurants = new StringBuilder();
        List<Restaurant> restaurantList = state.getRestaurants();
        if (restaurantList != null) {
            for (Restaurant rest : restaurantList) {
                restaurants
                        .append("- ")
                        .append(rest.getName())
                        .append(" (평균 가격: ")
                        .append(String.format("%,d", rest.getPrice()))
                        .append("원)\n")
                        .append("  위치: ")
                        .append(rest.getAddress())
                        .append("\n")
                        .append("  메뉴: ")
                        .append(rest.getDescription())
                        .append("\n");
            }
        }

        StringBuilder accommodations = new StringBuilder();
        List<Accommodation> accommodationList = state.getAccommodations();
        if (accommodationList != null) {
            for (Accommodation acc : accommodationList) {
                accommodations
                        .append("- ")
                        .append(acc.getName())
                        .append(" (1박: ")
                        .append(String.format("%,d", acc.getPricePerNight()))
                        .append("원)\n")
                        .append("  위치: ")
                        .append(acc.getAddress())
                        .append("\n")
                        .append("  특징: ")
                        .append(acc.getDescription())
                        .append("\n");
            }
        }

        Integer days = state.getDays();
        Integer maxBudget = state.getMaxBudget();
        String prompt = USER_PROMPT_TEMPLATE
                .replace("{days}", String.valueOf(days))
                .replace("{nights}", String.valueOf(days - 1))
                .replace("{budget}", String.format("%,d", maxBudget))
                .replace("{attractions}", attractions.toString())
                .replace("{restaurants}", restaurants.toString())
                .replace("{accommodations}", accommodations.toString());

        boolean replan = state.isReplan();
        Integer previousTotalCost = state.getPreviousTotalCost();
        if (replan && previousTotalCost != null) {
            int exceededAmount = previousTotalCost - maxBudget;
            String replanWarning = String.format("""          
                            **예산 재계획 필수**
                            이전 일정이 예산을 %,d원 초과했습니다. (이전 총비용: %,d원)
                            반드시 더 저렴한 관광지, 맛집, 숙소를 선택하여
                            총 예산 %,d원 이내로 일정을 재작성해야 합니다.
                            가능한 한 무료 또는 저렴한 관광지를 우선 선택하고,
                            식사와 숙소도 가격이 낮은 옵션을 선택하세요.
                            """,
                    exceededAmount,
                    previousTotalCost,
                    maxBudget);
            prompt = prompt + replanWarning;
        }

        return prompt;
    }
}
