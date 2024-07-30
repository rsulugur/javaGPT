package com.ai.gpt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponse(List<Product> productList, List<ErrorObject> errors) {
}
