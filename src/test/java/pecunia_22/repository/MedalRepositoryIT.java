package pecunia_22.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.models.repositories.MedalRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.util.List;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MedalRepositoryIT {

    private static MedalRepository medalRepository;

    @Autowired
    public MedalRepositoryIT(
            MedalRepository medalRepository
    ) {
        this.medalRepository = medalRepository;
    }

    @Test
    void shouldAggregateMedalsByCountryAndRespectVisibleFilter() {

        // given
        String status = "KOLEKCJA";

        // when
        List<CountryByStatus> all =
                medalRepository.countryByStatus(status, null);

        List<CountryByStatus> visibleTrue =
                medalRepository.countryByStatus(status, true);

        List<CountryByStatus> visibleFalse =
                medalRepository.countryByStatus(status, false);

        // then
        assertThat(all).isNotNull();

        long sumAll = all.stream()
                .mapToLong(CountryByStatus::total)
                .sum();

        long sumTrue = visibleTrue.stream()
                .mapToLong(CountryByStatus::total)
                .sum();

        long sumFalse = visibleFalse.stream()
                .mapToLong(CountryByStatus::total)
                .sum();

        // ADMIN widzi wszystko
        assertThat(sumAll).isGreaterThanOrEqualTo(sumTrue);
        assertThat(sumAll).isGreaterThanOrEqualTo(sumFalse);

        // USER + niewidoczne = ALL
        assertThat(sumTrue + sumFalse).isEqualTo(sumAll);

        log.info("""
        🟢 [IT][MEDAL] countryByStatus verification
        ALL     -> {}
        TRUE    -> {}
        FALSE   -> {}
        """,
                sumAll,
                sumTrue,
                sumFalse
        );
    }

    @Test
    void shouldReturnEmptyListForNonExistingStatus() {

        List<CountryByStatus> result =
                medalRepository.countryByStatus("NIE_ISTNIEJE", null);

        assertThat(result).isEmpty();
        log.info("""
                 🟢 [IT][MEDAL] countryByStatus verification
                 RESULT     -> {}
                """,
                result
                );

    }

    @Test
    void shouldRespectVisibleAndCountryFiltersInCurrencyByStatus() {

        // given
        String status = "KOLEKCJA";

        // znajdź kraj, dla którego są jakieś wyniki
        List<CurrencyByStatus> any =
                medalRepository.currencyByStatus(status, 244L, null);

        if (any.isEmpty()) {
            // jeśli brak danych dla tego kraju – test pomijamy
            return;
        }

        Long countryId = any.get(0).countryId();

        // when
        List<CurrencyByStatus> all =
                medalRepository.currencyByStatus(status, countryId, null);

        List<CurrencyByStatus> visibleTrue =
                medalRepository.currencyByStatus(status, countryId, true);

        List<CurrencyByStatus> visibleFalse =
                medalRepository.currencyByStatus(status, countryId, false);

        // then
        long sumAll = all.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumTrue = visibleTrue.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumFalse = visibleFalse.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        // ADMIN widzi wszystko
        assertThat(sumAll).isGreaterThanOrEqualTo(sumTrue);
        assertThat(sumAll).isGreaterThanOrEqualTo(sumFalse);

        // suma widocznych i niewidocznych = wszystkie
        assertThat(sumTrue + sumFalse).isEqualTo(sumAll);

        log.info("""
        🟢 [IT][MEDAL] currencyByStatus logic verification
        COUNTRY -> {}
        ALL     -> {}
        TRUE    -> {}
        FALSE   -> {}
        """,
                countryId,
                sumAll,
                sumTrue,
                sumFalse
        );
    }

    @Test
    void shouldReturnEmptyForInvalidStatus() {

        List<CurrencyByStatus> result =
                medalRepository.currencyByStatus("NIE_ISTNIEJE", 244L, null);

        assertThat(result).isEmpty();
    }
}