package chocola.springai.service.ch14;

import chocola.springai.dto.ch14.Plan;
import chocola.springai.dto.ch14.PlanState;
import chocola.springai.dto.ch14.Requirements;
import chocola.springai.service.ch13.agent.Exam05AttractionAgent;
import chocola.springai.service.ch13.agent.Exam06RestaurantAgent;
import chocola.springai.service.ch13.agent.Exam07AccommodationAgent;
import chocola.springai.service.ch13.dto.Accommodation;
import chocola.springai.service.ch13.dto.Attraction;
import chocola.springai.service.ch13.dto.Restaurant;
import chocola.springai.service.ch14.agent.BudgetAgent;
import chocola.springai.service.ch14.agent.PlanAgent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class TravelOrchestrator {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final Exam05AttractionAgent attractionAgent;
    private final Exam06RestaurantAgent restaurantAgent;
    private final Exam07AccommodationAgent accommodationAgent;
    private final PlanAgent planAgent;
    private final BudgetAgent budgetAgent;
    private final InheritableThreadLocal<SseEmitter> threadLocal;

    public TravelOrchestrator(ChatClient.Builder chatClientBuilder,
                              ChatMemory chatMemory,
                              Exam05AttractionAgent attractionAgent,
                              Exam06RestaurantAgent restaurantAgent,
                              Exam07AccommodationAgent accommodationAgent,
                              PlanAgent planAgent,
                              BudgetAgent budgetAgent) {
        this.chatClient = chatClientBuilder.build();
        this.chatMemory = chatMemory;
        this.attractionAgent = attractionAgent;
        this.restaurantAgent = restaurantAgent;
        this.accommodationAgent = accommodationAgent;
        this.planAgent = planAgent;
        this.budgetAgent = budgetAgent;
        this.threadLocal = new InheritableThreadLocal<>();
    }

    public String execute(String userQuery, String conversationId, SseEmitter emitter) {
        threadLocal.set(emitter);

        String systemMessage = """
                  당신은 여행 계획 전문 오케스트레이터(조율자)입니다.
                  사용자의 질문을 분석하여 적절한 전문 에이전트에게 작업을 위임합니다.
                
                  ## 사용 가능한 도구 (Agent):
                  1. callAttractionAgent - 관광지/명소 추천
                      예: "서울 명소", "부산 관광지", "제주도 가볼만한 곳"
                  2. callRestaurantAgent - 맛집 추천
                      예: "강남 맛집", "제주도 흑돼지", "부산 해물 맛집"
                  3. callAccommodationAgent - 숙소 추천
                      예: "서울 호텔", "제주도 펜션", "가성비 숙소"
                  4. callMultiAgentForTravelPlan - 전체 여행 일정 생성
                      예: "제주도 2박3일 100만원 예산으로 계획 짜줘"
                
                  ## 응답 규칙:
                  - 단일 정보(관광지/맛집/숙소)만 필요하면 해당 도구 하나만 호출
                  - "N박N일 계획" 요청 시 callMultiAgentForTravelPlan 호출
                  - 도구 호출이 필요 없는 일반 질문은 직접 답변
                
                  당신의 주 역할은 적절한 도구(전문가 에이전트)를 호출하는 것입니다.
                """;

        String response = chatClient.prompt()
                .system(systemMessage)
                .user(userQuery)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(this)
                .call()
                .content();

        threadLocal.remove();

        return response;
    }

    private void sendSseEvent(String agentName, String status, String message) {
        try {
            SseEmitter emitter = threadLocal.get();
            if (emitter != null) {
                emitter.send(SseEmitter
                        .event()
                        .name("agent")
                        .data(Map.of(
                                "agent", agentName,
                                "status", status,
                                "message", message)));
            }
        } catch (Exception ignored) {
        }
    }

    @Tool(description = """
            관광지 정보를 조회합니다.
            사용자가 관광지, 명소, 가볼만한 곳에 대해 물어볼 때 이 도구를 사용하세요.
            예: "제주도 명소", "제주도 관광지", "제주도 가볼만한 곳"
            """, returnDirect = true)
    public List<Attraction> callAttractionAgent(String query) {
        sendSseEvent("AttractionAgent", "running", "관광지 정보 검색 중...");
        List<Attraction> attractions = attractionAgent.execute(query);
        sendSseEvent("AttractionAgent", "complete", "관광지 정보 검색 완료");

        return attractions;
    }

    @Tool(description = """
            맛집 정보를 조회합니다.
            사용자가 음식, 맛집, 레스토랑에 대해 물어볼 때 이 도구를 사용하세요.
            예: "제주도 맛집", "제주도 흑돼지 맛집", "제주도 해물 맛집"
            """, returnDirect = true)
    public List<Restaurant> callRestaurantAgent(String query) {
        sendSseEvent("RestaurantAgent", "running", "맛집 정보 검색 중...");
        List<Restaurant> restaurants = restaurantAgent.execute(query);
        sendSseEvent("RestaurantAgent", "complete", "맛집 정보 검색 완료");

        return restaurants;
    }

    @Tool(description = """
            숙소 정보를 조회합니다.
            사용자가 호텔, 숙소, 펜션, 리조트에 대해 물어볼 때 이 도구를 사용하세요.
            예: "제주도 호텔", "제주도 펜션", "제주도 가성비 숙소"
            """, returnDirect = true)
    public List<Accommodation> callAccommodationAgent(String query) {
        sendSseEvent("AccommodationAgent", "running", "숙소 정보 검색 중...");
        List<Accommodation> accommodations = accommodationAgent.execute(query);
        sendSseEvent("AccommodationAgent", "complete", "숙소 정보 검색 완료");

        return accommodations;
    }

    @Tool(description = """
            전체 여행 일정을 생성합니다.
            사용자가 "N박N일 계획 짜줘", "여행 일정 만들어줘"라고 요청할 때 사용하세요.
            예: "제주도 2박3일 여행 계획 짜줘", "제주도 3박4일 여행 일정 만들어줘"
            """, returnDirect = true)
    public Plan callMultiAgentForTravelPlan(String query) {
        PlanState state = parseUserQuery(query);

        collectTravelInfoInParallel(state);

        sendSseEvent("PlanAgent", "running", "초기 여행 일정 생성 중...");
        planAgent.execute(state);
        sendSseEvent("PlanAgent", "complete", "초기 여행 일정 생성 완료");

        sendSseEvent("BudgetAgent", "running", "예산 분석 중...");
        budgetAgent.execute(state);
        sendSseEvent("BudgetAgent", "complete", state.getBudgetAnalysis().message());

        if (state.getBudgetAnalysis().exceeded()) {
            sendSseEvent("PlanAgent", "warning", "예산 초과 - 재계획 필요");
            replanWithAdjustedBudget(state);
        }

        return state.getPlan();
    }

    private PlanState parseUserQuery(String userQuery) {
        PlanState state = new PlanState();
        state.setUserQuery(userQuery);

        // LLM을 사용하여 구조화된 정보 추출
        String userMessage = String.format("""
                다음 사용자 질문에서 여행 정보를 추출하여 JSON 형식으로 반환하세요.
                
                사용자 질문: "%s"
                
                추출할 정보:
                - destination: 여행지, 예) 제주도
                - days: 여행 전체 일수, 예) 3, 4
                - maxBudget: 예산 (숫자만, 원 단위), 예) 200000, 1000000
                
                추출 규칙:
                - destination은 "제주도"로 고정값
                - days는 "N박N일"에서 마지막 숫자 추출 (예: "2박3일" → 3)
                - maxBudget은 원 단위 숫자로 변환:
                  * "20만원" → 200000 (20 곱하기 10000)
                  * "100만원" → 1000000 (100 곱하기 10000)
                  * "50만원" → 500000 (50 곱하기 10000)
                  * "200만원" → 2000000 (200 곱하기 10000)
                
                예시 변환:
                - "제주도 2박3일 20만원" → {"destination": "제주도", "days": 3, "maxBudget": 200000}
                - "제주도 3박4일 100만원" → {"destination": "제주도", "days": 4, "maxBudget": 1000000}
                - "제주도 1박2일 50만원" → {"destination": "제주도", "days": 2, "maxBudget": 500000}
                
                **중요**: "X만원"에서 X는 만원 단위 숫자입니다. "20만원"은 20×10000=200000원입니다.
                
                JSON만 반환하고 다른 설명은 추가하지 마세요.
                """, userQuery);

        Requirements requirements = chatClient.prompt()
                .user(userMessage)
                .call()
                .entity(Requirements.class);

        if (requirements != null) {
            state.setDestination(requirements.destination());
            state.setDays(requirements.days());
            state.setMaxBudget(requirements.maxBudget());
        }

        return state;
    }

    private void collectTravelInfoInParallel(PlanState state) {
        final SseEmitter emitter = threadLocal.get();

        CompletableFuture<Void> attractionTask = CompletableFuture.runAsync(() -> {
            threadLocal.set(emitter);
            sendSseEvent("AttractionAgent", "running", "관광지 검색 중...");
            attractionAgent.execute(state);
            sendSseEvent("AttractionAgent", "complete", "관광지 검색 완료");
        });

        CompletableFuture<Void> restaurantTask = CompletableFuture.runAsync(() -> {
            threadLocal.set(emitter);
            sendSseEvent("RestaurantAgent", "running", "맛집 검색 중...");
            restaurantAgent.execute(state);
            sendSseEvent("RestaurantAgent", "complete", "맛집 검색 완료");
        });

        CompletableFuture<Void> accommodationTask = CompletableFuture.runAsync(() -> {
            threadLocal.set(emitter);
            sendSseEvent("AccommodationAgent", "running", "숙소 검색 중...");
            accommodationAgent.execute(state);
            sendSseEvent("AccommodationAgent", "complete", "숙소 검색 완료");
        });

        CompletableFuture.allOf(attractionTask, restaurantTask, accommodationTask).join();
    }

    private void replanWithAdjustedBudget(PlanState state) {
        sendSseEvent("PlanAgent", "warning", "더 저렴한 옵션으로 재계획 중...");

        state.setReplan(true);
        state.setPreviousTotalCost(state.getPlan().getTotalCost());

        final SseEmitter emitter = threadLocal.get();

        CompletableFuture<Void> attractionReSearch = CompletableFuture.runAsync(() -> {
            threadLocal.set(emitter);

            try {
                sendSseEvent("AttractionAgent", "running", "관광지 재검색 중...");
                attractionAgent.execute(state);
                sendSseEvent("AttractionAgent", "complete", "관광지 재검색 완료");
            } catch (Exception ignored) {
            }
        });

        CompletableFuture<Void> accommodationReSearch = CompletableFuture.runAsync(() -> {
            threadLocal.set(emitter);

            try {
                sendSseEvent("AccommodationAgent", "running", "숙소 재검색 중...");
                accommodationAgent.execute(state);
                sendSseEvent("AccommodationAgent", "complete", "숙소 재검색 완료");
            } catch (Exception ignored) {
            }
        });

        CompletableFuture<Void> restaurantReSearch = CompletableFuture.runAsync(() -> {
            threadLocal.set(emitter);

            try {
                sendSseEvent("RestaurantAgent", "running", "맛집 재검색 중...");
                restaurantAgent.execute(state);
                sendSseEvent("RestaurantAgent", "complete", "맛집 재검색 완료");
            } catch (Exception ignored) {
            }
        });

        CompletableFuture.allOf(attractionReSearch, restaurantReSearch, accommodationReSearch).join();

        sendSseEvent("PlanAgent", "running", "일정 재계획 중...");
        planAgent.execute(state);
        sendSseEvent("PlanAgent", "complete", "일정 재계획 완료");

        sendSseEvent("BudgetAgent", "running", "일정 재계획 예산 검증");
        budgetAgent.execute(state);
        sendSseEvent("BudgetAgent", "complete", state.getBudgetAnalysis().message());
    }
}
