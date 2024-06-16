package pecunia_22.services.coinService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pecunia_22.models.Coin;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.models.sqlClass.GetCoinsByStatus;

import java.util.List;

@Service
public interface CoinService {
    List<Coin> getAllCoin();
    void saveCoin(Coin coin);
    Coin getCoinById(Long id);
    void deleteCoinById(Long id);

    List<Coin> getCoinByCurrencyId(Long currencyId);
    List<Coin> getCoinByCurrencyId(Long currencyId, String role);
    List<CountryByStatus> getCountryByStatus(String status);
    List<CountryByStatus> getCountryByStatus(String status, String role);
    List<CurrencyByStatus> getCurrencyByStatus(Long countryId, String status, String role);

    List<GetCoinsByStatus> getCoinsByStatus(String status, Long countryId);
    List<GetCoinsByStatus> getCoinsByStatus(String status);
    List<GetCoinsByStatus> getCoinsByStatus(String status, String statusSell);

    Page<Coin> findCoinPaginated(Integer pageNo, Integer pageSize, Long currencyId, String status, String role);
}
