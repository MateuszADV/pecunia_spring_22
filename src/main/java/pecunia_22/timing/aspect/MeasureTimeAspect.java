package pecunia_22.timing.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pecunia_22.timing.annotation.MeasureTime;

@Slf4j
@Aspect
@Component
public class MeasureTimeAspect {
    @Around("@annotation(measureTime)")
    public Object measureExecutionTime(
            ProceedingJoinPoint joinPoint,
            MeasureTime measureTime
    ) throws Throwable {

        long start = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;

            if (measureTime.thresholdMs() > 0 && duration < measureTime.thresholdMs()) {
                // tylko nie logujemy – NIE return!
            } else {
                logExecution(joinPoint, measureTime, duration);
            }
        }

        return result;
    }

    private void logExecution(
            ProceedingJoinPoint joinPoint,
            MeasureTime measureTime,
            long duration
    ) {
        String methodName = joinPoint.getSignature().toShortString();
        String description = measureTime.value();

        String colorPrefix = getColorPrefix(measureTime.color());
        String colorSuffix = getColorSuffix(measureTime.color());

        String message = String.format(
                "%s\n⏱ [MeasureTime] %s %s executed in %d ms%s",
                colorPrefix,
                description.isEmpty() ? "" : "[" + description + "]",
                methodName,
                duration,
                colorSuffix
        );

        switch (measureTime.level()) {
            case INFO -> log.info(message);
            case WARN -> log.warn(message);
            case DEBUG -> log.debug(message);
        }
    }


//    @Around("@annotation(measureTime)")
//    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, MeasureTime measureTime) throws Throwable {
//        long start = System.currentTimeMillis();
//        Object result = joinPoint.proceed();
//        long end = System.currentTimeMillis();
//        long duration = end - start;
//
//        // jeśli threshold ustawiony i czas < threshold, nie loguj
//        if (measureTime.thresholdMs() > 0 && duration < measureTime.thresholdMs()) {
//            return result;
//        }
//
//        String methodName = joinPoint.getSignature().toShortString();
//        String description = measureTime.value();
//        String colorPrefix = getColorPrefix(measureTime.color());
//        String colorSuffix = getColorSuffix(measureTime.color());
//
//        String message = String.format("\n%s⏱ [MeasureTime] %s %s executed in %d ms%s",
//                colorPrefix,
//                description.isEmpty() ? "" : "[" + description + "]",
//                methodName,
//                duration,
//                colorSuffix
//        );
//
//        switch (measureTime.level()) {
//            case INFO -> log.info(message);
//            case WARN -> log.warn(message);
//            case DEBUG -> log.debug(message);
//        }
//
//        return result;
//    }



    private String getColorPrefix(MeasureTime.ConsoleColor color) {
        return switch (color) {
            case GREEN -> "\u001B[32m";  // zielony
            case YELLOW -> "\u001B[33m"; // żółty
            case RED -> "\u001B[31m";    // czerwony
            default -> "";
        };
    }

    private String getColorSuffix(MeasureTime.ConsoleColor color) {
        return color == MeasureTime.ConsoleColor.NONE ? "" : "\u001B[0m"; // reset koloru
    }

}
