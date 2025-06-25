package com.ipaam.logging.aspect;

import com.ipaam.logging.model.LogEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class LoggingAspect {
    private static final Log logger = LogFactory.getLog(LoggingAspect.class);

    @Around("@within(com.ipaam.logging.annotation.LogClass)||@annotation(com.ipaam.logging.annotation.LogMethod)")
    public Object logMethodDetails(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        // Capture start time for execution time calculation
        long startTime = System.currentTimeMillis();

        // Proceed with method execution
        Object result = joinPoint.proceed();

        // Capture execution time
        long executionTime = System.currentTimeMillis() - startTime;

        // Get user context (optional, if user authentication is involved)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "Anonymous";

        // Optional: Capture request IP address (if required)
        String ipAddress = "127.0.0.1"; // Replace with actual request IP if needed (via HttpServletRequest)

        // Create LogEntry record
        LogEntry logEntry = new LogEntry(methodName, arguments, executionTime, result, username, ipAddress);

        // Log the structured log entry (logs it as a JSON string due to Logback's JSON encoder)
        logger.info("Log Entry: " + logEntry);

        return result;
    }

}
