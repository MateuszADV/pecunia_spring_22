package pecunia_22.repository;


import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.models.repositories.SecurityRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SecurityRepositoryIT {

    private final SecurityRepository securityRepository;
    private final EntityManager entityManager;

    @Autowired
    public SecurityRepositoryIT(SecurityRepository securityRepository, EntityManager entityManager) {
        this.securityRepository = securityRepository;
        this.entityManager = entityManager;
    }

    @Test
    void shouldLoadCurrencyByStatus_whenVisibleTrue() {
        // given
        String status = "KOLEKCJA";
        Long countryId = 172l;
        Boolean visible = true;

        // when
        List<CurrencyByStatus> result =
                securityRepository.currencyByStatus(status, countryId, visible);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        // sprawdzenie agregacji
        assertThat(result)
                .allMatch(dto -> dto.total() > 0);

        log.info(
                "\n🟢 [IT][SECURITY] currencyByStatus -> {} rows (status={}, countryId={}, visible=true)",
                result.size(), status, countryId
        );
    }

    @Test
    void shouldLoadCurrencyByStatus_whenVisibleIsNull() {
        // given
        String status = "KOLEKCJA";
        Long countryId = 244L;
        Boolean visible = null;

        // when
        List<CurrencyByStatus> result =
                securityRepository.currencyByStatus(status, countryId, visible);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        // sprawdzenie agregacji
        assertThat(result)
                .allMatch(dto -> dto.total() > 0);

        log.info(
                "\n🟢 [IT][SECURITY] currencyByStatus -> {} rows (status={}, countryId={}, visible=null)",
                result.size(), status, countryId
        );
    }

    @Test
    void shouldLoadCurrencyByStatus_whenVisibleIsFalse() {
        // given
        String status = "KOLEKCJA";
        Long countryId = 244L;
        Boolean visible = false;

        // when
        List<CurrencyByStatus> result =
                securityRepository.currencyByStatus(status, countryId, visible);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        // sprawdzenie agregacji
        assertThat(result)
                .allMatch(dto -> dto.total() > 0);

        log.info(
                "\n🟢 [IT][SECURITY] currencyByStatus -> {} rows (status={}, countryId={}, visible=false)",
                result.size(), status, countryId
        );
    }

    @Test
    void shouldFilterProperlyByVisibleParameter() {

        // given
        String status = "KOLEKCJA";
        Long countryId = 244L;

        // when
        List<CurrencyByStatus> all =
                securityRepository.currencyByStatus(status, countryId, null);

        List<CurrencyByStatus> visibleTrue =
                securityRepository.currencyByStatus(status, countryId, true);

        List<CurrencyByStatus> visibleFalse =
                securityRepository.currencyByStatus(status, countryId, false);

        // then
        assertThat(all).isNotEmpty();

        // visible true i false nie mogą mieć więcej rekordów niż all
        assertThat(visibleTrue.size()).isLessThanOrEqualTo(all.size());
        assertThat(visibleFalse.size()).isLessThanOrEqualTo(all.size());

        log.info("""
        🟢 [IT][SECURITY] visible filter verification
        ALL     -> {}
        TRUE    -> {}
        FALSE   -> {}
        """,
                all.size(),
                visibleTrue.size(),
                visibleFalse.size()
        );
    }

    @Test
    void shouldValidateCountryByStatus_visibleTrueFalseAndNull() {

        // given
        String status = "KOLEKCJA";

        // when
        List<CountryByStatus> visibleTrue =
                securityRepository.countryByStatus(status, true);

        List<CountryByStatus> visibleFalse =
                securityRepository.countryByStatus(status, false);

        List<CountryByStatus> all =
                securityRepository.countryByStatus(status, null);

        // then
        assertThat(all).isNotNull();
        assertThat(all).isNotEmpty();

        // suma total dla każdego wariantu
        long totalTrue = visibleTrue.stream()
                .mapToLong(CountryByStatus::total)
                .sum();

        long totalFalse = visibleFalse.stream()
                .mapToLong(CountryByStatus::total)
                .sum();

        long totalAll = all.stream()
                .mapToLong(CountryByStatus::total)
                .sum();

        // kluczowa asercja logiczna
        assertThat(totalTrue + totalFalse)
                .isEqualTo(totalAll);

        log.info("""
            
            🟢 [IT][SECURITY] countryByStatus
               true  -> rows={}, total={}
               false -> rows={}, total={}
               null  -> rows={}, total={}
            """,
                visibleTrue.size(), totalTrue,
                visibleFalse.size(), totalFalse,
                all.size(), totalAll
        );
    }


}