package pecunia_22.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import utils.JsonUtils;

import java.util.*;
import java.util.regex.Pattern;

@Aspect
@Component
public class SecureJsonMvcLoggerAspect {

    private static final Logger log = LoggerFactory.getLogger(SecureJsonMvcLoggerAspect.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 🔒 Słowa kluczowe, które będą maskowane w logach
    private static final Pattern SENSITIVE_KEYS = Pattern.compile(
            "(?i)(password|pass|email|token|secret|auth|session|key)"
    );

    @Around("within(@org.springframework.stereotype.Controller *)")
    public Object logMvcControllersSecurely(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (attrs != null) ? attrs.getRequest() : null;

        String clientIp = (request != null) ? request.getRemoteAddr() : "unknown";
        String uri = (request != null) ? request.getRequestURI() : "unknown";
        String method = (request != null) ? request.getMethod() : "N/A";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";

        Object[] args = joinPoint.getArgs();

        // 📦 Model size
        int modelSize = Arrays.stream(args)
                .filter(a -> a instanceof Model)
                .mapToInt(a -> ((Model) a).asMap().size())
                .findFirst()
                .orElse(0);

        // 🧾 Dane wejściowe w trybie DEBUG (z maskowaniem)
        if (log.isDebugEnabled()) {
            log.debug(toJson(Map.of(
                    "event", "ControllerEntry",
                    "controller", className,
                    "method", methodName,
                    "user", username,
                    "ip", clientIp,
                    "uri", uri,
                    "httpMethod", method,
                    "args", maskSensitive(argsToMap(args))
            )));
        } else {
            log.info("\n🎬 [{}#{}] {} {} | IP={} | User='{}'", className, methodName, method, uri, clientIp, username);
        }

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error(toJson(Map.of(
                    "event", "ControllerError",
                    "controller", className,
                    "method", methodName,
                    "user", username,
                    "ip", clientIp,
                    "uri", uri,
                    "error", e.getMessage()
            )), e);
            throw e;
        }

        long duration = System.currentTimeMillis() - start;
        String viewName = (result instanceof String) ? (String) result : "unknown";

        Map<String, Object> logData = new LinkedHashMap<>();
        logData.put("event", "ControllerExit");
        logData.put("controller", className);
        logData.put("method", methodName);
        logData.put("view", viewName);
        logData.put("modelSize", modelSize);
        logData.put("durationMs", duration);
        logData.put("ip", clientIp);
        logData.put("user", username);
        logData.put("uri", uri);
//        logData.put("test",auth);

        if (log.isDebugEnabled()) {
            logData.put("model", maskSensitive(extractModelMap(args)));
        }

        log.info(toJson(logData));
        System.out.println(JsonUtils.gsonPretty(logData));

        if (duration > 1000) {
            log.warn(toJson(Map.of(
                    "event", "SlowRequest",
                    "view", viewName,
                    "durationMs", duration,
                    "user", username,
                    "ip", clientIp
            )));
        }

        return result;
    }

    // 🔧 Pomocnicze metody
    private Map<String, Object> argsToMap(Object[] args) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            map.put("arg" + i, args[i]);
        }
        return map;
    }

    private Map<String, Object> extractModelMap(Object[] args) {
        return Arrays.stream(args)
                .filter(a -> a instanceof Model)
                .findFirst()
                .map(a -> ((Model) a).asMap())
                .orElse(Collections.emptyMap());
    }

    private Map<String, Object> maskSensitive(Map<String, Object> input) {
        Map<String, Object> masked = new LinkedHashMap<>();
        input.forEach((k, v) -> {
            if (SENSITIVE_KEYS.matcher(k).find()) {
                masked.put(k, "******");
            } else if (v instanceof Map) {
                masked.put(k, maskSensitive((Map<String, Object>) v));
            } else {
                masked.put(k, v);
            }
        });
        return masked;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"json-serialization-failed\"}";
        }
    }
}
