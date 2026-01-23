package pecunia_22.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class MvcRequestContextLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(MvcRequestContextLoggingAspect.class);

    @Around("within(@org.springframework.stereotype.Controller *)")
    public Object logMvcControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        Object[] args = joinPoint.getArgs();

        // 🧩 Pobranie requesta (jeśli istnieje)
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (attrs != null) ? attrs.getRequest() : null;

        String clientIp = (request != null) ? request.getRemoteAddr() : "unknown";
        String requestUri = (request != null) ? request.getRequestURI() : "unknown";

        // 🔐 Pobranie użytkownika z kontekstu Spring Security
        String username = "anonymous";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
        }

        log.info("\n🎬 [{}#{}] Wejście z IP={} | User='{}' | URI={} | Args={}",
                className, methodName, clientIp, username, requestUri, Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error("\n❌ [{}#{}] Błąd: {} | IP={} | User='{}'", className, methodName, e.getMessage(), clientIp, username, e);
            throw e;
        }

        long duration = System.currentTimeMillis() - start;

        // 📦 Wyciągnięcie informacji o modelu i widoku
        int modelSize = Arrays.stream(args)
                .filter(a -> a instanceof Model)
                .mapToInt(a -> ((Model) a).asMap().size())
                .findFirst()
                .orElse(0);

        String viewName = (result instanceof String) ? (String) result : "unknown";

        log.info("\n✅ [{}#{}] Widok='{}' | ModelSize={} | IP={} | User='{}' | URI={} | Time={}ms",
                className, methodName, viewName, modelSize, clientIp, username, requestUri, duration);

        // Ostrzeżenie przy wolnych żądaniach
        if (duration > 1000) {
            log.warn("\n🐢 [SlowView] '{}' renderowany w {} ms (User='{}', IP={})",
                    viewName, duration, username, clientIp);
        }

        return result;
    }

//    // 🔸 Punkt przecięcia dla wszystkich kontrolerów @Controller
////    @Around("within(@org.springframework.stereotype.Controller *)")
//    @Around("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
//
//    public Object logMvcControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
//
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        String className = signature.getDeclaringType().getSimpleName();
//        String methodName = signature.getName();
//        Object[] args = joinPoint.getArgs();
//
//        log.info("\n🎬 [{}#{}] Wejście do metody z argumentami: {}", className, methodName, Arrays.toString(args));
//
//        Object result;
//        try {
//            result = joinPoint.proceed();
//        } catch (Exception e) {
//            log.error("\n❌ [{}#{}] Wystąpił wyjątek: {}", className, methodName, e.getMessage(), e);
//            throw e;
//        }
//
//        long duration = System.currentTimeMillis() - start;
//
//        // 🔹 Sprawdź, czy metoda ma argument typu Model
//        int modelSize = Arrays.stream(args)
//                .filter(a -> a instanceof Model)
//                .mapToInt(a -> ((Model) a).asMap().size())
//                .findFirst()
//                .orElse(0);
//
//        String viewName = (result instanceof String) ? (String) result : "unknown";
//
//        log.info("\n✅ [{}#{}] Zakończono w {} ms | Widok: '{}' | Rozmiar modelu: {}",
//                className, methodName, duration, viewName, modelSize);
//
//        return result;
//    }
}


