package pecunia_22.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pecunia_22.models.Currency;
import pecunia_22.models.dto.currency.CurrencyDtoWithCount;
import pecunia_22.models.repositories.CurrencyRepository;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
@ActiveProfiles("test")
@Slf4j
class CurrencyRepositoryIT {

    private final CurrencyRepository currencyRepository;

    private static final List<String> PATTERNS = List.of("MEDAL", "COIN", "NOTE", "SECURITY");


    @Autowired
    public CurrencyRepositoryIT(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Test
    void shouldFindCurrencyByCountryAndPattern_realDB() {

        // given (dopasuj do swojej bazy!)
        Long countryId = 244L;
        String pattern = "NOTE";

        // when
        List<Currency> result =
                currencyRepository.getCurrencyByCountryByPattern(countryId, pattern);

        // log
        log.info("=== RESULT SIZE: {}", result.size());

        result.forEach(c ->
                log.info("""
                        Currency:
                        id -> {}
                        series -> {}
                        country -> {}
                        pattern -> {}
                        """,
                        c.getId(),
                        c.getCurrencySeries(),
                        c.getCountries().getCountryEn(),
                        c.getPatterns().getPattern()
                )
        );

        // then
        assertNotNull(result);
    }

    @Test
    void testGetCurrencyWithDynamicCount() {
        Long countryId = 103L;
        String pattern = "MEDAL";

        List<CurrencyDtoWithCount> results = currencyRepository.getCurrencyWithDynamicCount(countryId, pattern);

        assertThat(results).isNotEmpty();

        // Wypiszemy wyniki w konsoli, żeby zweryfikować COUNT
        for (CurrencyDtoWithCount dto : results) {
            log.info("""
                    ID -> {}
                    Series -> {}
                    Pattern -> {}
                    Country -> {}
                    Count -> {}
                    """,
                    dto.getId(),
                    dto.getCurrencySeries(),
                    dto.getPattern(),
                    dto.getCountryEn(),
                    dto.getElementsCount()
            );
        }
    }

    @Test
    void testGetCurrencyWithDynamicCountForAllPatterns() {
        Long countryId = 172L; // przykładowy ID kraju

        for (String pattern : PATTERNS) {
            log.info("==== Testing pattern: {} ====", pattern);

            List<CurrencyDtoWithCount> results = currencyRepository.getCurrencyWithDynamicCount(countryId, pattern);

            if (results.isEmpty()) {
                log.info("No currencies found for pattern {}", pattern);
            } else {
                results.forEach(dto -> log.info("""
                        
                        ID -> {}
                        Series -> {}
                        Pattern -> {}
                        Country -> {}
                        Count -> {}
                        """,
                        dto.getId(),
                        dto.getCurrencySeries(),
                        dto.getPattern(),
                        dto.getCountryEn(),
                        dto.getElementsCount()
                ));
            }
        }
    }

}