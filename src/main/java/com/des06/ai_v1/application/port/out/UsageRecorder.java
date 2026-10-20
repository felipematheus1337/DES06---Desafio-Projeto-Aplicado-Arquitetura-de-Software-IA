package com.des06.ai_v1.application.port.out;

import com.des06.ai_v1.application.model.UsageEvent;

public interface UsageRecorder {

    boolean record(UsageEvent event);
}
