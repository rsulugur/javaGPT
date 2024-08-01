package com.ai.gpt.service;

import com.ai.gpt.mapper.AuditRepository;
import com.ai.gpt.model.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository repository;

    public void save(Audit model) {
        repository.save(model);
    }

    public List<Audit> findAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    // Other service methods
}
