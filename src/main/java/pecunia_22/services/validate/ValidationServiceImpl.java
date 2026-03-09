package pecunia_22.services.validate;

import org.springframework.stereotype.Service;
import pecunia_22.exceptions.ResourceNotFoundException;
import pecunia_22.models.repositories.CountryRepository;
import pecunia_22.models.repositories.CurrencyRepository;
import pecunia_22.models.repositories.MedalRepository;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.util.List;


@Service
public class ValidationServiceImpl implements ValidationService {
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final MedalRepository medalRepository;

    public ValidationServiceImpl(
            CountryRepository countryRepository,
            CurrencyRepository currencyRepository,
            MedalRepository medalRepository) {

        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
        this.medalRepository = medalRepository;
    }

    @Override
    public void validateCountry(Long countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new RuntimeException("Country not found");
        }
    }

    @Override
    public void validateCurrency(Long currencyId) {
        if (!currencyRepository.existsById(currencyId)) {
            throw new RuntimeException("Currency not found");
        }
    }

    @Override
    public void validateMedal(Long medalId) {
        if (!medalRepository.existsById(medalId)) {
            throw new RuntimeException("Medal not found");
        }
    }

    @Override
    public void validateVisibleCurrencyForUser(Long countryId, String status) {

        List<CurrencyByStatus> result =
                medalRepository.currencyByStatus(status, countryId, true);

        if(result.isEmpty()){
            throw new ResourceNotFoundException("No visible data for this country");
        }
    }
}
