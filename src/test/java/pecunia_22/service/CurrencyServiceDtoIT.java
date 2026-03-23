package pecunia_22.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pecunia_22.models.dto.currency.CurrencyDtoByPattern;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.services.currencyService.CurrencyService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
//@Transactional
@ActiveProfiles("test")
public class CurrencyServiceDtoIT {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ModelMapper modelMapper;

    private final Long testCountryId = 244L; // Twoje testowe państwo
    private final String testCountryEn = "AATEST";
    private final String testPattern = "NOTE";
//    private final String testCountryEn1 = "Poland";

    @Test
    void testGetCurrencyByCountryAndPatternDto() {
        List<CurrencyDtoByPattern> dtos = currencyService.getCurrencyByCountryAndPatternDto(testCountryId, testPattern);

        log.info("=== RESULT SIZE: {}", dtos.size());
        dtos.forEach(c -> log.info("Currency ID: {}, Series: {}, Country: {}, Pattern: {}",
                c.getId(), c.getCurrencySeries(), c.getCountries().getCountryEn(), c.getPattern()));

        assertThat(dtos).isNotEmpty();
        assertThat(dtos.get(0).getCountries()).isNotNull();
    }

    @Test
    void testGetCurrencyByCountryEnAndPatternDto() {
        List<CurrencyDtoByPattern> dtos = currencyService.getCurrencyByCountryEnAndPatternDto(testCountryEn, testPattern);

        log.info("=== RESULT SIZE: {}", dtos.size());
        dtos.forEach(c -> log.info("Currency ID: {}, Series: {}, Country: {}, Pattern: {}",
                c.getId(), c.getCurrencySeries(), c.getCountries().getCountryEn(), c.getPattern()));

        assertThat(dtos).isNotEmpty();
    }

    @Test
    void testGetCurrencyDtoById() {
        Long currencyId = 787L; // Przykładowe ID z bazy
        CurrencyDto dto = currencyService.getCurrencyDtoById(currencyId);

        log.info("Currency DTO: ID: {}, Series: {}, Pattern: {}", dto.getId(), dto.getCurrencySeries(), dto.getPattern());
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(currencyId);
    }

    @Test
    void testGetAllCurrencyDto() {
        List<CurrencyDto> dtos = currencyService.getAllCurrencyDto();

        log.info("=== TOTAL CURRENCIES: {}", dtos.size());
        assertThat(dtos).isNotEmpty();
    }
}