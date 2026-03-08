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
import pecunia_22.models.Medal;
import pecunia_22.models.repositories.MedalRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.services.medalService.MedalServiceImpl;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MedalRepositoryIT {

    private static MedalRepository medalRepository;
    private static MedalServiceImpl medalService;

    @Autowired
    public MedalRepositoryIT(
            MedalRepository medalRepository,
            MedalServiceImpl medalService
    ) {
        this.medalRepository = medalRepository;
        this.medalService = medalService;
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


        @Test
        void shouldDemonstrateLastWriteWins() throws Exception {

            // given
            Medal medal = medalRepository.findById(1L).orElseThrow();

            ExecutorService executor = Executors.newFixedThreadPool(2);
            CountDownLatch latch = new CountDownLatch(2);

            Runnable task1 = () -> {
                try {
                    Medal m1 = medalRepository.findById(1L).orElseThrow();
                    m1.setDescription("THREAD_1");
                    medalService.updateMedal(m1);
                } finally {
                    latch.countDown();
                }
            };

            Runnable task2 = () -> {
                try {
                    Medal m2 = medalRepository.findById(1L).orElseThrow();
                    m2.setDescription("THREAD_2");
                    medalService.updateMedal(m2);
                } finally {
                    latch.countDown();
                }
            };

            // when
            executor.submit(task1);
            executor.submit(task2);

            latch.await();
            executor.shutdown();

            // then
            Medal updated = medalRepository.findById(1L).orElseThrow();

            System.out.println("Final description: " + updated.getDescription());
        }

    @Test
    void shouldReturnOnlyVisibleMedalsWithGivenStatusAndPaginate() {

        // given
        Long currencyId = 244L; // przykładowe ID z Twojej bazy
        String status = "KOLEKCJA";

        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<Medal> page = medalRepository
                .medalPageable(currencyId, status, true, pageable);

        // then
        assertThat(page).isNotNull();

        // 1️⃣ sprawdzamy filtr visible
        assertThat(page.getContent())
                .allMatch(m -> Boolean.TRUE.equals(m.getVisible()));

        // 2️⃣ sprawdzamy filtr status
        assertThat(page.getContent())
                .allMatch(m -> status.equals(m.getStatuses().getStatus()));

        // 3️⃣ sprawdzamy sortowanie
        List<Double> denominations = page.getContent()
                .stream()
                .map(Medal::getDenomination)
                .toList();

        List<Double> sorted = denominations.stream()
                .sorted()
                .toList();

        assertThat(denominations).isEqualTo(sorted);
    }

    @Test
    void shouldReturnOnlyVisibleMedalsWithGivenStatusAndPaginateLog() {

        Long currencyId = 778L;
        String status = "KOLEKCJA";
        Pageable pageable = PageRequest.of(0, 5);

        log.info("Testing medalPageable with currencyId={}, status={}, visible=true",
                currencyId, status);

        Page<Medal> page = medalRepository
                .medalPageable(currencyId, status, true, pageable);

        log.info("Total elements: {}", page.getTotalElements());
        log.info("Total pages: {}", page.getTotalPages());
        log.info("Current page size: {}", page.getContent().size());

        page.getContent().forEach(m ->
                log.info("Medal id={}, denomination={}, visible={}, status={}",
                        m.getId(),
                        m.getDenomination(),
                        m.getVisible(),
                        m.getStatuses().getStatus()
                )
        );

        assertThat(page.getContent())
                .allMatch(m -> Boolean.TRUE.equals(m.getVisible()));

        assertThat(page.getContent())
                .allMatch(m -> status.equals(m.getStatuses().getStatus()));
    }
}