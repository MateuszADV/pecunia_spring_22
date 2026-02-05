package pecunia_22.timing.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)    // adnotacja może być stosowana tylko do metod
@Retention(RetentionPolicy.RUNTIME)  // adnotacja jest dostępna w czasie działania programu (runtime)
@Documented  // powoduje, że adnotacja pojawia się w javadoc
public @interface MeasureTime {
    String value() default "";           // opis operacji
    long thresholdMs() default 0;        // loguj tylko jeśli czas > threshold
    LogLevel level() default LogLevel.INFO; // poziom logowania
    ConsoleColor color() default ConsoleColor.NONE; // kolor w konsoli

    enum LogLevel { INFO, WARN, DEBUG }
    enum ConsoleColor { NONE, GREEN, YELLOW, RED }
}
