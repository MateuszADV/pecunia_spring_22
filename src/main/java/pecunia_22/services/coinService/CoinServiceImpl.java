package pecunia_22.services.coinService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pecunia_22.models.Coin;
import pecunia_22.models.repositories.CoinRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.models.sqlClass.GetCoinsByStatus;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoinServiceImpl implements CoinService {
    private CoinRepository coinRepository;

    @Autowired
    public CoinServiceImpl(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    @Override
    public List<Coin> getAllCoin() {
        return coinRepository.findAll();
    }

    @Override
    public void saveCoin(Coin coin) {
        this.coinRepository.save(coin);
    }

    @Override
    public Coin getCoinById(Long id) {
        Optional<Coin> optional = coinRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Quality Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteCoinById(Long id) {
        this.coinRepository.deleteById(id);
    }

    @Override
    public List<Coin> getCoinByCurrencyId(Long currencyId) {
        List<Coin> coins = coinRepository.getCoinByCurrencyId(currencyId);
        return coins;
    }

    @Override
    public List<Coin> getCoinByCurrencyId(Long currencyId, String role) {
        List<Coin> coins = new ArrayList<>();
        if (role == "ADMIN") {
            coins = coinRepository.getCoinByCurrencyId(currencyId);
        } else {
            coins = coinRepository.getCoinByCurrencyId(currencyId, true);
        }
        return coins;
    }

    @Override
    public List<CountryByStatus> getCountryByStatus(String status, String role) {
        List<Object[]> objects = new ArrayList<>();
        List<CountryByStatus> countryByStatusList = new ArrayList<>();

        if (role == "ADMIN") {
            objects = coinRepository.countryByStatus(status);
            for (Object[] object : objects) {
                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
            }
        } else {
            objects = coinRepository.countryByStatus(status, true);
            for (Object[] object : objects) {
                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
            }
        }
        return countryByStatusList;
    }

    @Override
    public List<GetCoinsByStatus> getCoinsByStatus(String status) {
        List<Object[]> objects = coinRepository.getCoinsByStatus(status);
        List<GetCoinsByStatus> getCoinsByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getCoinsByStatusList.add(new ModelMapper().map(object[0], GetCoinsByStatus.class));
        }
        return getCoinsByStatusList;
    }

    @Override
    public List<CurrencyByStatus> getCurrencyByStatus(Long countryId, String status, String role) {
        List<Object[]> objects = new ArrayList<>();
        List<CurrencyByStatus> currencyByStatusList = new ArrayList<>();

        if (role == "ADMIN") {
            currencyByStatusList = coinRepository.currencyByStatus(status, countryId, null);
//            objects = coinRepository.currencyByStatus(status, countryId, null);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        } else {
            currencyByStatusList = coinRepository.currencyByStatus(status, countryId, true);
//            objects = coinRepository.currencyByStatus(status, countryId, true);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        }
        return currencyByStatusList;
    }

    @Override
    public Page<Coin> findCoinPaginated(Integer pageNo, Integer pageSize, Long currencyId, String status, String role) {
        List<Coin> coins = new ArrayList<>();
        if (role == "ADMIN") {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.coinRepository.coinPageable(currencyId, status, pageable);
        } else {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.coinRepository.coinPageable(currencyId, status, true, pageable);
        }
    }

    @Override
    public List<CountryByStatus> getCountryByStatus(String status) {

        List<Object[]> objects;
        List<CountryByStatus> countryByStatusList = new ArrayList<>();
        objects = coinRepository.countryByStatus(status);

        System.out.println(JsonUtils.gsonPretty(objects));
        for (Object[] object : objects) {
            countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
        }
        return countryByStatusList;
    }

    @Override
    public List<GetCoinsByStatus> getCoinsByStatus(String status, Long countryId) {
        List<Object[]> objects;
        objects = coinRepository.getCoinsByStatus(status, countryId);
        List<GetCoinsByStatus> getCoinsByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getCoinsByStatusList.add(new ModelMapper().map(object[0],GetCoinsByStatus.class));
        }
        System.out.println(JsonUtils.gsonPretty(getCoinsByStatusList));
        return getCoinsByStatusList;
    }

    @Override
    public List<GetCoinsByStatus> getCoinsByStatus(String status, String statusSell) {
        List<Object[]> objects = coinRepository.getCoinsByStatus(status, statusSell);
        List<GetCoinsByStatus> getCoinsByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getCoinsByStatusList.add(new ModelMapper().map(object[0], GetCoinsByStatus.class));
        }
        return getCoinsByStatusList;
    }

    @Override
    public void updateCoin(Coin coin) {
        coinRepository.updateCoin(coin.getCurrencies().getId(), coin.getDenomination(), coin.getDateBuy(), coin.getNameCurrency(), coin.getSeries(),
                coin.getBoughts().getId(), coin.getItemDate(), coin.getQuantity(), coin.getUnitQuantity(), coin.getActives().getId(), coin.getPriceBuy(), coin.getPriceSell(),
                coin.getQualities().getId(), coin.getDiameter(), coin.getThickness(), coin.getWeight(), coin.getStatuses().getId(), coin.getImageTypes().getId(),
                coin.getStatusSell(), coin.getVisible(), coin.getComposition(), coin.getDescription(), coin.getAversPath(), coin.getReversePath(),
                coin.getId());
    }
}
