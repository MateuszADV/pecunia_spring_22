package utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.models.Country;
import pecunia_22.models.dto.country.CountryGetDto;
import pecunia_22.services.countryService.CountryServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Component
public class Search {
    public static void searchCountry(@RequestParam("keyword") String keyword, ModelMap modelMap, CountryServiceImpl countryService) {
        if (keyword.isEmpty()) {
            List<CountryGetDto> countryGetDtos = new ArrayList<>();
            List<Country> countries = countryService.getCountryByCountryEnAsc();
            for (Country country : countries) {
                countryGetDtos.add(new ModelMapper().map(country, CountryGetDto.class));
            }
            modelMap.addAttribute("countries", countryGetDtos);
        }
        modelMap.addAttribute("keyword", keyword);
        List<Country> countries = countryService.searchCountry(keyword);
        List<CountryGetDto> countryGetDtos = new ArrayList<>();
        for (Country country : countries) {
            countryGetDtos.add(new ModelMapper().map(country, CountryGetDto.class));
        }
        modelMap.addAttribute("countries", countryGetDtos);
    }
}
