package pecunia_22.repository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pecunia_22.models.Country;
import pecunia_22.models.Currency;
import pecunia_22.models.Pattern;
import pecunia_22.models.repositories.CurrencyRepository;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
//@Transactional
@ActiveProfiles("test")
@Slf4j
class CurrencyRepositoryIT {

    private final CurrencyRepository currencyRepository;

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
}