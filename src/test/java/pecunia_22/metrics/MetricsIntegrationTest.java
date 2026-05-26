package pecunia_22.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pecunia_22.services.countryService.CountryService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class MetricsIntegrationTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void shouldRegisterRepositoryMetrics() {

        log.info("=== TEST START ===");

        // wykonanie metody
        countryService.getAllCountries();

        Timer timer = meterRegistry
                .find("repository.execution.time")
                .timer();

        if (timer == null) {

            log.error("Metric repository.execution.time NOT FOUND");

        } else {

            log.info("Metric FOUND");

            log.info("Count: {}", timer.count());

            log.info("Total Time (ms): {}",
                    timer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS));

            log.info("Max Time (ms): {}",
                    timer.max(java.util.concurrent.TimeUnit.MILLISECONDS));
        }

        log.info("=== TEST END ===");
    }
}