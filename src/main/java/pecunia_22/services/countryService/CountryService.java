package pecunia_22.services.countryService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pecunia_22.models.Country;
import pecunia_22.models.sqlClass.Continent;
import pecunia_22.models.sqlClass.CountryCount;

import java.util.List;

@Service
public interface CountryService {
    List<Country> getAllCountries();
    void saveCountry(Country country);
    Country getCountryById(Long id);
    void deleteCountryById(Long id);
    Page<Country> findPaginated(Integer pageNo, Integer pageSize, String sortField, String sortDirection);
    List<Country> getCountriesWithContinent(String continentEn);
    List<Country> getCountryByCountryEnAsc();
    List<Country> searchCountry(String keyWord);
    Country getCountyByCountryEn(String countryEn);

    //    *****************************************
//    ******Query związane z countries*********
//    *****************************************
    List<CountryCount> countryCounts();

    List<Continent> getTotalCountry();
}
