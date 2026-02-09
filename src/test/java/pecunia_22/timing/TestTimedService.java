package pecunia_22.timing;

import org.springframework.stereotype.Service;
import pecunia_22.timing.annotation.MeasureTime;

@Service
public class TestTimedService {

    @MeasureTime(value = "test-method", thresholdMs = 0)
    public String fastMethod() {
        return "OK";
    }

    @MeasureTime(value = "slow-method", thresholdMs = 10)
    public String slowMethod() throws InterruptedException {
        Thread.sleep(20);
        return "SLOW_OK";
    }
}
