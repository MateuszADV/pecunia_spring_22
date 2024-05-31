package pecunia_22.services.countryService;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pecunia_22.models.Continent;
import pecunia_22.models.Country;
import pecunia_22.models.repositories.ContinentRepository;
import pecunia_22.models.repositories.CountryRepository;
import pecunia_22.models.sqlClass.CountryCount;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepository countryRepository;
    private ContinentRepository continentRepository;

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public void saveCountry(Country country) {
        this.countryRepository.save(country);
    }

    @Override
    public Country getCountryById(Long id) {
        Optional<Country> optional = countryRepository.findById(id);
        Country country = new Country();
        if (optional.isPresent()) {
            country = optional.get();
        }else {
            throw new RuntimeException("Country Not Found For Id :: " + id);
        }
        return country;
    }

    @Override
    public void deleteCountryById(Long id) {
        this.countryRepository.deleteById(id);
    }

    @Override
    public Page<Country> findPaginated(Integer pageNo, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.countryRepository.findAll(pageable);
    }

    @Override
    public List<Country> getCountriesWithContinent(String continentEn) {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(continentEn);
        Continent continent = continentRepository.getContinent(continentEn);

        List<Country> countries = countryRepository.countries(continent.getContinentPl());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return countries;
    }

    @Override
    public List<Country> getCountryByCountryEnAsc() {
        return countryRepository.countriesOrderByCountryEnAsc();
    }

    @Override
    public List<Country> searchCountry(String keyWord) {
        return countryRepository.searchCountry(keyWord);
    }

    @Override
    public Country getCountyByCountryEn(String countryEn) {
        Country country = countryRepository.findByCountryEn(countryEn);
        if (country != null){
            return country;
        }
        return null;
    }


//    *****************************************
//    ******Query związane z countries*********
//    *****************************************

    @Override
    public List<CountryCount> countryCounts() {

        List<Object[]> objects = countryRepository.countCountry();
        List<CountryCount> countryCounts = new ArrayList<>();

        for (Object[] object : objects) {
            countryCounts.add(new ModelMapper().map(object[0], CountryCount.class));
        }
        System.out.println(JsonUtils.gsonPretty(countryCounts));

        return countryCounts;
    }

    @Override
    public List<pecunia_22.models.sqlClass.Continent> getTotalCountry() {
        List<Object[]> objects = countryRepository.countryByContinent();
        List<pecunia_22.models.sqlClass.Continent> continents = new ArrayList<>();
        for (Object[] object : objects) {
            continents.add(new ModelMapper().map(object[0], pecunia_22.models.sqlClass.Continent.class));
        }
        System.out.println(JsonUtils.gsonPretty(continents));
        return continents;
    }
}
