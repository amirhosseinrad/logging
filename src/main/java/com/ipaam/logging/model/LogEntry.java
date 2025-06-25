package com.ipaam.logging.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)  // Exclude null fields from being logged
public record LogEntry(
        String methodName,
        Object[] arguments,
        long executionTime,
        Object result,
        String username,
        String ipAddress
) {
}
