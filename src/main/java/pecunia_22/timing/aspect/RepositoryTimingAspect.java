package pecunia_22.timing.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev","test"})
@Slf4j
@Aspect
@Component
public class RepositoryTimingAspect {

    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    @Around("execution(public * pecunia_22.models.repositories..*(..))")
    public Object measureRepositoryMethodTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.nanoTime();

        Object result = joinPoint.proceed();

        long elapsed = (System.nanoTime() - start) / 1_000_000;
        String methodName = joinPoint.getSignature().toShortString();

        if (elapsed < 50) {
            log.info("""
                    
                    {}[REPO][FAST] {} -> {} ms{}
                    """,
                    GREEN,
                    methodName,
                    elapsed,
                    RESET);

        } else if (elapsed < 150) {
            log.info("""
                    
                    {}[REPO][SLOW] {} -> {} ms{}
                    """,
                    YELLOW,
                    methodName,
                    elapsed,
                    RESET);

        } else {
            log.info("""
                    
                    {}[REPO][VERY SLOW] {} -> {} ms{}""",
                    RED,
                    methodName,
                    elapsed,
                    RESET);
        }

        return result;
    }

//    @Around("execution(public * pecunia_22.models.repositories.NoteRepository.*(..))")
//    public Object measureRepositoryMethodTimeNote(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        long start = System.currentTimeMillis();
//
//        Object result = joinPoint.proceed();
//
//        long elapsed = System.currentTimeMillis() - start;
//        String methodName = joinPoint.getSignature().toShortString();
//
//        // ANSI colors
//        String green = "\u001B[32m";
//        String yellow = "\u001B[33m";
//        String red = "\u001B[31m";
//        String reset = "\u001B[0m";
//
//        String color;
//        String level;
//
//        if (elapsed < 50) {
//            color = green;
//            level = "FAST";
//        } else if (elapsed < 150) {
//            color = yellow;
//            level = "SLOW";
//        } else {
//            color = red;
//            level = "VERY SLOW";
//        }
//
//        log.info(
//                color + "\n🕒 [REPO][{}] {} executed in {} ms" + reset,
//                level, methodName, elapsed
//        );
//
//        return result;
//    }
//
////    @Around("execution(public * pecunia_22.models.repositories.NoteRepository.*(..))")
////    public Object measureRepositoryMethodTimeNote(ProceedingJoinPoint joinPoint) throws Throwable {
////        long start = System.currentTimeMillis();
////
////        Object result = joinPoint.proceed();
////
////        long elapsed = System.currentTimeMillis() - start;
////        String methodName = joinPoint.getSignature().toShortString();
////
////        // prosty kolorowy log – zielony jeśli szybciej niż 50ms, czerwony jeśli wolniej
////        String color = elapsed > 50 ? "\u001B[31m" : "\u001B[32m"; // ANSI: 32=green, 31=red
////        String reset = "\u001B[0m";
////
////        log.info(color + "\n🕒 [REPO] {} executed in {} ms" + reset, methodName, elapsed);
////
////        return result;
////    }
//
////    @Around("execution(public * pecunia_22.models.repositories.CoinRepository.*(..))")
////    public Object measureRepositoryMethodTimeCoin(ProceedingJoinPoint joinPoint) throws Throwable {
////        long start = System.currentTimeMillis();
////
////        Object result = joinPoint.proceed();
////
////        long elapsed = System.currentTimeMillis() - start;
////        String methodName = joinPoint.getSignature().toShortString();
////
////        // prosty kolorowy log – zielony jeśli szybciej niż 50ms, czerwony jeśli wolniej
////        String color = elapsed > 50 ? "\u001B[31m" : "\u001B[32m"; // ANSI: 32=green, 31=red
////        String reset = "\u001B[0m";
////
////        log.info(color + "\n🕒 [REPO] {} executed in {} ms" + reset, methodName, elapsed);
////
////        return result;
////    }
//
//    @Around("execution(public * pecunia_22.models.repositories.CoinRepository.*(..))")
//    public Object measureRepositoryMethodTimeCoin(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        long start = System.currentTimeMillis();
//
//        Object result = joinPoint.proceed();
//
//        long elapsed = System.currentTimeMillis() - start;
//        String methodName = joinPoint.getSignature().toShortString();
//
//        // ANSI colors
//        String green = "\u001B[32m";
//        String yellow = "\u001B[33m";
//        String red = "\u001B[31m";
//        String reset = "\u001B[0m";
//
//        String color;
//        String level;
//
//        if (elapsed < 50) {
//            color = green;
//            level = "FAST";
//        } else if (elapsed < 150) {
//            color = yellow;
//            level = "SLOW";
//        } else {
//            color = red;
//            level = "VERY SLOW";
//        }
//
//        log.info(
//                color + "\n🕒 [REPO][{}] {} executed in {} ms" + reset,
//                level, methodName, elapsed
//        );
//
//        return result;
//    }
//
//    @Around("execution(public * pecunia_22.models.repositories.SecurityRepository.*(..))")
//    public Object measureRepositoryMethodTimeSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        long start = System.currentTimeMillis();
//
//        Object result = joinPoint.proceed();
//
//        long elapsed = System.currentTimeMillis() - start;
//        String methodName = joinPoint.getSignature().toShortString();
//
//        // ANSI colors
//        String green = "\u001B[32m";
//        String yellow = "\u001B[33m";
//        String red = "\u001B[31m";
//        String reset = "\u001B[0m";
//
//        String color;
//        String level;
//
//        if (elapsed < 50) {
//            color = green;
//            level = "FAST";
//        } else if (elapsed < 150) {
//            color = yellow;
//            level = "SLOW";
//        } else {
//            color = red;
//            level = "VERY SLOW";
//        }
//
//        log.info(
//                color + "\n🕒 [REPO][{}] {} executed in {} ms" + reset,
//                level, methodName, elapsed
//        );
//
//        return result;
//    }
//
//    @Around("execution(public * pecunia_22.models.repositories.MedalRepository.*(..))")
//    public Object measureRepositoryMethodTimeMedal(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        long start = System.currentTimeMillis();
//
//        Object result = joinPoint.proceed();
//
//        long elapsed = System.currentTimeMillis() - start;
//        String methodName = joinPoint.getSignature().toShortString();
//
//        // ANSI colors
//        String green = "\u001B[32m";
//        String yellow = "\u001B[33m";
//        String red = "\u001B[31m";
//        String reset = "\u001B[0m";
//
//        String color;
//        String level;
//
//        if (elapsed < 50) {
//            color = green;
//            level = "FAST";
//        } else if (elapsed < 150) {
//            color = yellow;
//            level = "SLOW";
//        } else {
//            color = red;
//            level = "VERY SLOW";
//        }
//
//        log.info(
//                color + "\n🕒 [REPO][{}] {} executed in {} ms" + reset,
//                level, methodName, elapsed
//        );
//
//        return result;
//    }
//
//    @Around("execution(public * pecunia_22.models.repositories.CountryRepository.*(..))")
//    public Object measureRepositoryMethodTimeCountry(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        long start = System.currentTimeMillis();
//
//        Object result = joinPoint.proceed();
//
//        long elapsed = System.currentTimeMillis() - start;
//        String methodName = joinPoint.getSignature().toShortString();
//
//        // ANSI colors
//        String green = "\u001B[32m";
//        String yellow = "\u001B[33m";
//        String red = "\u001B[31m";
//        String reset = "\u001B[0m";
//
//        String color;
//        String level;
//
//        if (elapsed < 50) {
//            color = green;
//            level = "FAST";
//        } else if (elapsed < 150) {
//            color = yellow;
//            level = "SLOW";
//        } else {
//            color = red;
//            level = "VERY SLOW";
//        }
//
//        log.info(
//                color + "\n🕒 [REPO][{}] {} executed in {} ms" + reset,
//                level, methodName, elapsed
//        );
//
//        return result;
//    }
}
