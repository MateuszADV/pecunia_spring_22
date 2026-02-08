package pecunia_22.timing.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RepositoryTimingAspect {

    @Around("execution(public * pecunia_22.models.repositories.NoteRepository.*(..))")
    public Object measureRepositoryMethodTimeNote(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long elapsed = System.currentTimeMillis() - start;
        String methodName = joinPoint.getSignature().toShortString();

        // prosty kolorowy log – zielony jeśli szybciej niż 50ms, czerwony jeśli wolniej
        String color = elapsed > 50 ? "\u001B[31m" : "\u001B[32m"; // ANSI: 32=green, 31=red
        String reset = "\u001B[0m";

        log.info(color + "🕒 [REPO] {} executed in {} ms" + reset, methodName, elapsed);

        return result;
    }

    @Around("execution(public * pecunia_22.models.repositories.CoinRepository.*(..))")
    public Object measureRepositoryMethodTimeCoin(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long elapsed = System.currentTimeMillis() - start;
        String methodName = joinPoint.getSignature().toShortString();

        // prosty kolorowy log – zielony jeśli szybciej niż 50ms, czerwony jeśli wolniej
        String color = elapsed > 50 ? "\u001B[31m" : "\u001B[32m"; // ANSI: 32=green, 31=red
        String reset = "\u001B[0m";

        log.info(color + "🕒 [REPO] {} executed in {} ms" + reset, methodName, elapsed);

        return result;
    }
}
