package com.des06.ai_v1.application.port.out;

import com.des06.ai_v1.application.model.BudgetBalance;

public interface BudgetLedger {
    BudgetBalance load(String id);
    void save(BudgetBalance balance);
}
