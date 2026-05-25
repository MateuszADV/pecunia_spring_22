package pecunia_22.aop;

import io.micrometer.core.instrument.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RepositoryMetricsAspect {

    private final MeterRegistry meterRegistry;

    @Around("execution(* pecunia_22.models.repositories..*(..))")
    public Object measureRepo(ProceedingJoinPoint joinPoint) throws Throwable {

        String method = joinPoint.getSignature().toShortString();

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            return joinPoint.proceed();
        } finally {

            sample.stop(Timer.builder("repository.execution.time")
                    .tag("method", method)
                    .tag("layer", "repository")
                    .register(meterRegistry));

            // opcjonalny trace log
            String traceId = MDC.get("X-Correlation-Id");

            System.out.printf(
                    "[METRICS] %s | trace=%s%n",
                    method,
                    traceId
            );
        }
    }
}