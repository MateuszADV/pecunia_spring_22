package pecunia_22.services.currencyService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Currency;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.models.dto.currency.CurrencyDtoByPattern;

import java.util.List;

@Service
public interface CurrencyService {
    List<Currency> getAllCurrencis();
    void saveCurrency(Currency currency);
    Currency getCurrencyById(Long id);
    void deleteCurrencyById(Long id);
    List<Currency> getCurrencyByCountryByPattern(Long countryId, String pattern);
    List<Currency> getCurrencyByCountryByPattern(String countryEn, String pattern);

    // --- NOWE metody DTO ---
    List<CurrencyDtoByPattern> getCurrencyByCountryAndPatternDto(Long countryId, String pattern);
    List<CurrencyDtoByPattern> getCurrencyByCountryEnAndPatternDto(String countryEn, String pattern);
    List<CurrencyDtoByPattern> getCurrencyByCountryEnAndPatternIdDto(String countryEn, Long patternId);
    CurrencyDto getCurrencyDtoById(Long id);
    List<CurrencyDto> getAllCurrencyDto();
}
