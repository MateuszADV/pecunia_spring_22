package pecunia_22.services.currencyService;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.exceptions.ResourceNotFoundException;
import pecunia_22.mapper.MedalMapper;
import pecunia_22.models.Currency;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.models.dto.currency.CurrencyDtoByPattern;
import pecunia_22.models.repositories.CountryRepository;
import pecunia_22.models.repositories.CurrencyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<Currency> getAllCurrencis() {
        return currencyRepository.findAll();
    }

    @Override
    public void saveCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    @Override
    public Currency getCurrencyById(Long id) {
        Optional<Currency> currency = currencyRepository.findById(id);
        if (currency.isPresent()) {
            return currency.get();
        }else {
            throw new RuntimeException("Currency Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteCurrencyById(Long id) {
        currencyRepository.deleteById(id);
    }

    @Override
    public List<Currency> getCurrencyByCountryByPattern(Long countryId, String pattern) {
        return currencyRepository.getCurrencyByCountryByPattern(countryId, pattern);
    }

    @Override
    public List<Currency> getCurrencyByCountryByPattern(String countryEn, String pattern) {

        Long countryId = countryRepository.findByCountryEn(countryEn)
                .orElseThrow(() -> ResourceNotFoundException.forField("Country", "countryEn", countryEn))
                .getId();

        return getCurrencyByCountryByPattern(countryId, pattern);
    }

//    @Override
//    public List<Currency> getCurrencyByCountryByPattern(Long countryId, String pattern) {
//        List<Currency> currencies = currencyRepository.getCurrencyByCountryByPattern(countryId, pattern);
//        return currencies;
//    }
//
//    @Override
//    public List<Currency> getCurrencyByCountryByPattern(String countryEn, String pattern) {
//        List<Currency> currencies = currencyRepository.getCurrencyByCountryByPattern(countryEn, pattern);
//        return currencies;
//    }



    // --- NOWE METODY DTO ---

    /**
     * Zwraca CurrencyDtoByPattern dla danego countryId i pattern
     */
    @Override
    @Transactional(readOnly = true)  // utrzymuje sesję otwartą
    public List<CurrencyDtoByPattern> getCurrencyByCountryAndPatternDto(Long countryId, String pattern) {
        return currencyRepository.getCurrencyByCountryByPattern(countryId, pattern)
                .stream()
                .map(c -> modelMapper.map(c, CurrencyDtoByPattern.class))
                .collect(Collectors.toList());
    }

    /**
     * Zwraca CurrencyDtoByPattern dla danego countryEn i pattern
     */
    @Override
    @Transactional(readOnly = true)  // utrzymuje sesję otwartą
    public List<CurrencyDtoByPattern> getCurrencyByCountryEnAndPatternDto(String countryEn, String pattern) {
        return currencyRepository.getCurrencyByCountryByPattern(countryEn, pattern)
                .stream()
                .map(c -> modelMapper.map(c, CurrencyDtoByPattern.class))
                .collect(Collectors.toList());
    }

    /**
     * Zwraca CurrencyDtoByPatternId dla danego countryEn i pattern
     */
    @Override
    @Transactional(readOnly = true)  // utrzymuje sesję otwartą
    public List<CurrencyDtoByPattern> getCurrencyByCountryEnAndPatternIdDto(String countryEn, Long patternId) {
        return currencyRepository.getCurrencyByCountryByPatternId(countryEn, patternId)
                .stream()
                .map(c -> modelMapper.map(c, CurrencyDtoByPattern.class))
                .collect(Collectors.toList());
    }

    /**
     * Zwraca CurrencyDto dla podanego ID
     */
    @Override
    public CurrencyDto getCurrencyDtoById(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Currency", id));
        return modelMapper.map(currency, CurrencyDto.class);
    }

    /**
     * Zwraca listę wszystkich CurrencyDto
     */
    @Override
    public List<CurrencyDto> getAllCurrencyDto() {
        return currencyRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CurrencyDto.class))
                .collect(Collectors.toList());
    }
}
