package pecunia_22.services.validate;

public interface ValidationService {
    void validateCountry(Long countryId);

    void validateCurrency(Long currencyId);

    void validateMedal(Long medalId);

    void validateVisibleCurrencyForUser(Long countryId, String status);
}
