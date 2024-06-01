package pecunia_22.services.currencyService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Currency;

import java.util.List;

@Service
public interface CurrencyService {
    List<Currency> getAllCurrencis();
    void saveCurrency(Currency currency);
    Currency getCurrencyById(Long id);
    void deleteCurrencyById(Long id);
    List<Currency> getCurrencyByCountryByPattern(Long countryId, String pattern);
    List<Currency> getCurrencyByCountryByPattern(String countryEn, String pattern);
}
