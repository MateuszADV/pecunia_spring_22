package pecunia_22.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pecunia_22.models.dto.country.CountrySearchDto;
import pecunia_22.services.countryService.CountryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CountryServiceImplTest {

    @Autowired
    private CountryService countryService;

    @Test
    void shouldReturnCountriesWhenKeywordExists() {

        // given
        String keyword = "pol";

        // when
        List<CountrySearchDto> results =
                countryService.searchCountryDto(keyword);

        // then
        assertNotNull(results);
        assertFalse(results.isEmpty());

        results.forEach(dto -> {

            assertNotNull(dto.getId());
            assertNotNull(dto.getCountryEn());
            assertNotNull(dto.getCountryPl());

            boolean match =
                    dto.getCountryEn().toLowerCase().contains(keyword)
                            || dto.getCountryPl().toLowerCase().contains(keyword);

            assertTrue(match);
        });
    }

    @Test
    void shouldReturnEmptyListWhenNothingFound() {

        // given
        String keyword = "xxxxxxxx";

        // when
        List<CountrySearchDto> results =
                countryService.searchCountryDto(keyword);

        // then
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldReturnAllCountriesWhenKeywordIsNull() {

        // when
        List<CountrySearchDto> results =
                countryService.searchCountryDto(null);

        // then
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    void shouldReturnAllCountriesWhenKeywordIsEmpty() {

        // when
        List<CountrySearchDto> results =
                countryService.searchCountryDto("");

        // then
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

}
