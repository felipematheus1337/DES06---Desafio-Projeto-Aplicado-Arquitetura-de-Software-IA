package com.des06.ai_v1.adapter.in.web;

import com.des06.ai_v1.application.port.in.BudgetStatusUseCase;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("hybrid")
@RequestMapping("/api/ai/budget")
public class AiBudgetController {

    private final BudgetStatusUseCase budget;
    public AiBudgetController(BudgetStatusUseCase budget) {
        this.budget = budget;
    }

    @GetMapping
    public BudgetStatusUseCase.Status get() {
        return budget.status();
    }
}
