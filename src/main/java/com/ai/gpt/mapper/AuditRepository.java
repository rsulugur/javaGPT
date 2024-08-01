package com.ai.gpt.mapper;

import com.ai.gpt.model.Audit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditRepository extends MongoRepository<Audit, String> {
    // Custom query methods (if needed)
}
