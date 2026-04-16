package chocola.springai.controller;

import chocola.springai.service.AiServiceRoleAssignmentPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiControllerRoleAssignmentPrompt {

    private final AiServiceRoleAssignmentPrompt aiServiceRoleAssignmentPrompt;

    @PostMapping("/role-assignment")
    public Flux<String> roleAssignment(String requirements) {
        return aiServiceRoleAssignmentPrompt.roleAssignment(requirements);
    }
}
