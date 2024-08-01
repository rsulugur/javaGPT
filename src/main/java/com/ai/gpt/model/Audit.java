package com.ai.gpt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Document(collection = "audit")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Audit {
    @Id
    private String _id;
    private String searchQuery;
    private Long timeTaken;
    private Map<String, Integer> results;
    private LocalDateTime createdAt = LocalDateTime.now(ZoneId.systemDefault());
}
