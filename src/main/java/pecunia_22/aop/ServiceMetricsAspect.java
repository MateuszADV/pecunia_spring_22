package pecunia_22.aop;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ServiceMetricsAspect {

    private final MeterRegistry meterRegistry;

    @Around("execution(* pecunia_22.services..*(..))")
    public Object measureService(ProceedingJoinPoint joinPoint) throws Throwable {

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            return joinPoint.proceed();
        } finally {
            sample.stop(Timer.builder("service.execution.time")
                    .tag("method", joinPoint.getSignature().getName())
                    .register(meterRegistry));
        }
    }
}