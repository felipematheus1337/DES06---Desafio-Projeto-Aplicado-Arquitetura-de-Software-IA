package com.des06.ai_v1.adapter.out.mongo;

import com.des06.ai_v1.application.model.UsageEvent;
import com.des06.ai_v1.application.port.out.UsageRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class MongoUsageRecorder implements UsageRecorder {
    private static final Logger LOG = LoggerFactory.getLogger(MongoUsageRecorder.class);
    private final MongoTemplate mongo;

    public MongoUsageRecorder(MongoTemplate mongo) { this.mongo = mongo; }


    @Override
    public boolean record(UsageEvent e) {

        try {
            mongo.insert(new UsageDocument(e.requestId(), e.readerId(), e.bookId(),
                    e.task(), e.provider(), e.model(), e.inputTokens(),
                    e.outputTokens(), e.totalTokens(), e.durationMs(), e.createdAt()));
            return true;
        } catch (DataAccessException ex) {
            LOG.error("Falha ao gravar consumo; requestId={}", e.requestId());
            return false;
        }
    }
}


