package pecunia_22.config.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricsInitializer {

    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void initMetrics() {

        Timer.builder("repository.execution.time")
                .description("Repository execution time")
                .tag("layer", "repository")
                .register(meterRegistry);

        Timer.builder("service.execution.time")
                .description("Service execution time")
                .tag("layer", "service")
                .register(meterRegistry);

        Timer.builder("repository.execution.time")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);
    }
}