package pecunia_22.repository;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pecunia_22.models.Country;
import pecunia_22.models.dto.country.CountrySearchDto;
import pecunia_22.models.repositories.CountryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class CountryRepositoryIT {

    @Autowired
    private CountryRepository countryRepository;

    private static void accept(CountrySearchDto dto) {
        log.info("""
                                        
                        ID -> {}
                        Country EN -> {}
                        Country PL -> {}
                        """,
                dto.getId(),
                dto.getCountryEn(),
                dto.getCountryPl()
        );
    }

    @Test
    void shouldFindByCountryEn() {
        // given
        String keyword = "pol";

        // when
        List<Country> results = countryRepository.searchCountry(keyword);

        // then
        assertNotNull(results);
        assertFalse(results.isEmpty());

        results.forEach(c ->
                assertTrue(
                        c.getCountryEn().toLowerCase().contains(keyword)
                                || c.getCountryPl().toLowerCase().contains(keyword)
                )
        );

        results.forEach(c ->
                getInfo(c)
        );
    }

    @Test
    void shouldBeCaseInsensitive() {
        // given
        String keyword = "PoL";

        // when
        List<Country> results = countryRepository.searchCountry(keyword);

        // then
        assertNotNull(results);
        assertFalse(results.isEmpty());

        results.forEach(c ->
                getInfo(c)
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoMatch() {
        // given
        String keyword = "zzzz_not_existing";

        // when
        List<Country> results = countryRepository.searchCountry(keyword);

        // then
        assertNotNull(results);
        assertTrue(results.isEmpty());

        extracted(results);

        results.forEach(c ->
                getInfo(c)
        );
    }

    private static void extracted(List<Country> results) {
        log.info("""
                
                Search Size -. {}
                """,
                results.size());
    }

    @Test
    void shouldFindByCountryPl() {
        // given
        String keyword = "ger"; // np. Polska

        // when
        List<Country> results = countryRepository.searchCountry(keyword);

        // then
        assertNotNull(results);
        assertFalse(results.isEmpty());

        boolean found = results.stream()
                .anyMatch(c -> c.getCountryPl().toLowerCase().contains(keyword));

        assertTrue(found);

        extracted(results);

        results.forEach(c ->
                getInfo(c)
        );
    }

    private static void getInfo(Country c) {
        log.info("""
                
                EN: {}, PL: {}
                """,
                c.getCountryEn(),
                c.getCountryPl());
    }

    @Test
    void shouldSearchCountryDto() {

        // given
        String keyword = "pol";

        // when
        List<CountrySearchDto> results =
                countryRepository.searchCountryDto(keyword);

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

        results.forEach(CountryRepositoryIT::accept
        );
    }

    @Test
    void shouldReturnEmptyListWhenCountryNotFound() {

        // given
        String keyword = "xxxxxxxx";

        // when
        List<CountrySearchDto> results =
                countryRepository.searchCountryDto(keyword);

        // then
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldSearchCountryDtoIgnoreCase() {

        // given
        String keyword = "PoL";

        // when
        List<CountrySearchDto> results =
                countryRepository.searchCountryDto(keyword);

        // then
        assertFalse(results.isEmpty());

        results.forEach(CountryRepositoryIT::accept);
    }

    @Test
    void shouldSearchCountryDtoIgnoreCaseNull() {

        // given
        String keyword = null;

        // when
        List<CountrySearchDto> results =
                countryRepository.searchCountryDto(keyword);

        // then
        assertTrue(results.isEmpty());

        results.forEach(CountryRepositoryIT::accept);
    }
}
