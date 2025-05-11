package pecunia_22.services.medalService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pecunia_22.models.Coin;
import pecunia_22.models.Medal;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.util.List;

@Service
public interface MedalService {
    List<Medal> getAllMedal();
    void saveMedal(Medal medal);
    Medal getMedalById(Long id);
    void deleteMedalById(Long id);

    List<Medal> getMedalByCurrencyId(Long currencyId);

    void updateMedal(Medal medal);

    List<CountryByStatus> getCountryByStatus(String status, String role);
    List<CurrencyByStatus> getCurrencyByStatus(Long countryId, String status, String role);
    Page<Medal> findMedalPaginated(Integer pageNo, Integer pageSize, Long currencyId, String status, String role);

}
