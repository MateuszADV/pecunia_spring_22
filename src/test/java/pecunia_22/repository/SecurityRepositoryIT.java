package pecunia_22.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.models.Security;
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

    @Autowired
    public SecurityRepositoryIT(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    @Test
    void shouldLoadCurrencyByStatus_whenVisibleTrue() {
        // given
        String status = "KOLEKCJA";
        Long countryId = 172L;
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
        long sumAll = all.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumTrue = visibleTrue.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumFalse = visibleFalse.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        assertThat(sumTrue + sumFalse).isEqualTo(sumAll);
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

    @Test
    void shouldLoadSecuritiesByStatusWithFilters() {
        // given – przykładowe wartości
        String status = "FOR SELL";
        String excludedStatusSell = null;
        Long countryId = 244L; // dopasowane do danych w bazie testowej

        // when
        List<Object[]> result = securityRepository.getSecuritiesByStatus(status, excludedStatusSell, countryId);

        // log w konwencji zielonej kropki
        getInfo(result, status, excludedStatusSell, countryId);

        // then – sprawdzamy tylko, że są wyniki
        assertThat(result).isNotEmpty();

        // dodatkowy wariant – bez filtrów excludedStatusSell i countryId null
        List<Object[]> resultNoFilter = securityRepository.getSecuritiesByStatus(status, null, null);
        log.info("\n🟢 [IT][SECURITY] getSecuritiesByStatus (no filters) -> {} rows (status={})", resultNoFilter.size(), status);
        assertThat(resultNoFilter).isNotEmpty();
    }

    private static void getInfo(List<Object[]> result, String status, String excludedStatusSell, Long countryId) {
        log.info("\n🟢 [IT][NOTE] getNotesByStatus (custom query) -> {} rows (status={}, excludedStatusSell={}, countryId={})",
                result.size(), status, excludedStatusSell, countryId);
    }

    @Test
    void shouldMaintainSortingAcrossPagesByDenomination() {

        // given
        Long currencyId = 336L; // dopasuj pod swoje dane
        String status = "KOLEKCJA";

        Pageable firstPageable = PageRequest.of(0, 5);
        Pageable secondPageable = PageRequest.of(1, 5);

        // when
        Page<Security> firstPage = securityRepository.securityPageable(
                currencyId,
                status,
                null,
                firstPageable
        );

        Page<Security> secondPage = securityRepository.securityPageable(
                currencyId,
                status,
                null,
                secondPageable
        );

        // then
        assertThat(firstPage).isNotNull();
        assertThat(secondPage).isNotNull();

        assertThat(firstPage.getContent()).isNotEmpty();

        // sprawdzenie sortowania na pierwszej stronie
        List<Double> firstDenominations = firstPage.getContent()
                .stream()
                .map(Security::getDenomination)
                .toList();

        assertThat(firstDenominations).isSorted();

        // jeśli druga strona istnieje — sprawdzamy ciągłość sortowania
        if (!secondPage.getContent().isEmpty()) {

            List<Double> secondDenominations = secondPage.getContent()
                    .stream()
                    .map(Security::getDenomination)
                    .toList();

            assertThat(secondDenominations).isSorted();

            Double lastFromFirstPage =
                    firstDenominations.get(firstDenominations.size() - 1);

            Double firstFromSecondPage =
                    secondDenominations.get(0);

            assertThat(firstFromSecondPage)
                    .isGreaterThanOrEqualTo(lastFromFirstPage);

            log.info("🟢 [IT][SECURITY] Sorting continuity OK -> {} <= {}",
                    lastFromFirstPage, firstFromSecondPage);
        }

        log.info("🟢 [IT][SECURITY] Pagination continuity test -> firstPage={}, secondPage={}, totalElements={}",
                firstPage.getNumber(),
                secondPage.getNumber(),
                firstPage.getTotalElements());
    }


}