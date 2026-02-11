package pecunia_22.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.models.repositories.CoinRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CoinRepositoryIT {

    @Autowired
    private CoinRepository coinRepository;

    @Test
    void shouldLoadCurrencyByStatus_whenVisibleTrue() {
        // given
        String status = "KOLEKCJA";
        Long countryId = 172L;
        Boolean visible = true;

        // when
        List<CurrencyByStatus> result =
                coinRepository.currencyByStatus(status, countryId, visible);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        log.info(
                "\n🟢 [IT][COIN] currencyByStatus -> {} rows (status={}, countryId={}, visible=true)",
                result.size(), status, countryId
        );
    }

    @Test
    void shouldLoadCurrencyByStatus_whenVisibleIsNull() {
        // given
        String status = "KOLEKCJA";
        Long countryId = 172L;
        Boolean visible = null;

        // when
        List<CurrencyByStatus> result =
                coinRepository.currencyByStatus(status, countryId, visible);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        log.info(
                "\n🟢 [IT][COIN] currencyByStatus -> {} rows (status={}, countryId={}, visible=null)",
                result.size(), status, countryId
        );
    }

    @Test
    void shouldLoadCurrencyByStatus_whenVisibleIsFalse() {
        // given
        String status = "KOLEKCJA";
        Long countryId = 172L;
        Boolean visible = false;

        // when
        List<CurrencyByStatus> result =
                coinRepository.currencyByStatus(status, countryId, visible);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        log.info(
                "\n🟢 [IT][COIN] currencyByStatus -> {} rows (status={}, countryId={}, visible=false)",
                result.size(), status, countryId
        );
    }

    @Test
    void shouldLoadCountriesByStatus() {
        // given
        String status = "KOLEKCJA";

        // when
        List<CountryByStatus> result = coinRepository.countryByStatus(status);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        log.info(
                "\n🟢 [IT][COIN] countryByStatus -> {} rows (status={})",
                result.size(), status
        );
    }

    @Test
    void shouldLoadCountriesByStatusAndVisibleTrue() {
        // given
        String status = "KOLEKCJA";
        Boolean visible = true;

        // when
        List<CountryByStatus> result =
                coinRepository.countryByStatus(status, visible);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result)
                .allMatch(c -> c.getTotal() != null && c.getTotal() >= 0);

        log.info(
                "\n🟢 [IT][COIN] countryByStatus -> {} rows (status={}, visible={})",
                result.size(), status, visible
        );
    }
}
