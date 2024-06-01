package pecunia_22.services.currencyService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.Currency;
import pecunia_22.models.repositories.CurrencyRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private CurrencyRepository currencyRepository;

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
        List<Currency> currencies = currencyRepository.getCurrencyByCountryByPattern(countryId, pattern);
        return currencies;
    }

    @Override
    public List<Currency> getCurrencyByCountryByPattern(String countryEn, String pattern) {
        List<Currency> currencies = currencyRepository.getCurrencyByCountryByPattern(countryEn, pattern);
        return currencies;
    }
}
