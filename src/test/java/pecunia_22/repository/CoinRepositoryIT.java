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
import java.util.Map;

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

    @Test
    void shouldLoadCoinsByStatus_withoutFilters() {

        // given
        String status = "KOLEKCJA";

        // when
        List<Object[]> result = coinRepository.getCoinsByStatus(
                status,
                null,   // excludedStatusSell
                null    // countryId
        );

        // then
        assertThat(result)
                .as("Coins should be returned for status = %s", status)
                .isNotNull()
                .isNotEmpty();

        log.info("\n🟢 [IT][COIN] getCoinByStatus -> {} rows (status={})",
                result.size(), status);
    }

    @Test
    void shouldLoadCoinsByStatus_withCountryFilter() {

        // given
        String status = "KOLEKCJA";
        Long countryId = 172L; // dopasuj do danych
//        Long countryId = 228L; // dopasuj do danych

        // when
        List<Object[]> result = coinRepository.getCoinsByStatus(
                status,
                null,
                countryId
        );

        // then
        assertThat(result)
                .as("Coins should be returned for status=%s and countryId=%s",
                        status, countryId)
                .isNotNull();

        log.info("\n🟢 [IT][COIN] getCoinsByStatus -> {} rows (status={}, countryId={})",
                result.size(), status, countryId);
    }

    @Test
    void shouldLoadCoinsByStatus_withAllFilters() {

        // given
        String status = "FOR SELL";
        String excludedStatusSell = "";
        Long countryId =244L;

        // when
        List<Object[]> result = coinRepository.getCoinsByStatus(
                status,
                excludedStatusSell,
                countryId
        );

        // then
        assertThat(result)
                .as("Coins should be returned for status=%s, excludedStatusSell=%s and countryId=%s",
                        status, excludedStatusSell, countryId)
                .isNotNull();

        log.info("\n🟢 [IT][COIN] getCoinsByStatus -> {} rows (status={}, excludedStatusSell={}, countryId={})",
                result.size(), status, excludedStatusSell, countryId);
    }

    @Test
    void shouldLoadCoinsByStatus_withExcludedStatusSellFilters() {

        // given
        String status = "FOR SELL";
        String excludedStatusSell = "";

        // when
        List<Object[]> result = coinRepository.getCoinsByStatus(
                status,
                excludedStatusSell,
                null
        );

        // then
        assertThat(result)
                .as("Coins should be returned for status=%s, excludedStatusSell=%s and countryId=%s",
                        status, excludedStatusSell)
                .isNotNull();

        log.info("\n🟢 [IT][COIN] getCoinsByStatus -> {} rows (status={}, excludedStatusSell={})",
                result.size(), status, excludedStatusSell);
    }

}
