package pecunia_22.timing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MeasureTimeAspectIT {

    @Autowired
    private TestTimedService testTimedService;

    @Test
    void shouldExecuteMethodWithMeasureTimeAspect() {
        // when
        String result = testTimedService.fastMethod();

        // then
        assertThat(result).isEqualTo("OK");
    }

    @Test
    void shouldExecuteSlowMethodAndReturnResult() throws Exception {
        // when
        String result = testTimedService.slowMethod();

        // then
        assertThat(result).isEqualTo("SLOW_OK");
    }
}
