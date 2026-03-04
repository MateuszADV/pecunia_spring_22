package pecunia_22.services.medalService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pecunia_22.models.Coin;
import pecunia_22.models.Medal;
import pecunia_22.models.repositories.MedalRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.timing.annotation.MeasureTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedalServiceImpl implements MedalService {

    private MedalRepository medalRepository;

    @Autowired
    public MedalServiceImpl(MedalRepository medalRepository) {
        this.medalRepository = medalRepository;
    }

    @Override
    public List<Medal> getAllMedal() {
        return medalRepository.findAll();
    }

    @Override
    public void saveMedal(Medal medal) {
        this.medalRepository.save(medal);
    }

    @Override
    public Medal getMedalById(Long id) {
        Optional<Medal> optional = medalRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Medal Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteMedalById(Long id) {
        this.medalRepository.deleteById(id);
    }

    @Override
    public List<Medal> getMedalByCurrencyId(Long currencyId) {
        List<Medal> medals = medalRepository.getMedalByCurrencyId(currencyId);
        return medals;
    }

    @MeasureTime(value = "Update medal in DB",
            color = MeasureTime.ConsoleColor.GREEN)
    @Override
    @Transactional
    public void updateMedal(Medal medal) {
        medalRepository.updateMedal(medal);
    }

//    @Override
//    public void updateMedal(Medal medal) {
//        medalRepository.updateMedal(medal.getCurrencies().getId(), medal.getDenomination(), medal.getDateBuy(), medal.getNameCurrency(), medal.getSeries(),
//                medal.getBoughts().getId(), medal.getItemDate(), medal.getQuantity(), medal.getUnitQuantity(), medal.getActives().getId(), medal.getPriceBuy(), medal.getPriceSell(),
//                medal.getQualities().getId(), medal.getDiameter(), medal.getThickness(), medal.getWeight(), medal.getStatuses().getId(), medal.getImageTypes().getId(),
//                medal.getStatusSell(), medal.getVisible(), medal.getComposition(), medal.getDescription(), medal.getAversPath(), medal.getReversePath(),
//                medal.getId());
//    }

    @Override
    public List<CountryByStatus> getCountryByStatus(String status, String role) {
//        List<Object[]> objects = new ArrayList<>();
        List<CountryByStatus> countryByStatusList = new ArrayList<>();

        if (role == "ADMIN") {
            countryByStatusList = medalRepository.countryByStatus(status, null);
//            objects = medalRepository.countryByStatus(status);
//            for (Object[] object : objects) {
//                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
//            }
        } else {
            countryByStatusList = medalRepository.countryByStatus(status, true);
//            objects = medalRepository.countryByStatus(status, true);
//            for (Object[] object : objects) {
//                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
//            }
        }
        return countryByStatusList;
    }

    @Override
    public List<CurrencyByStatus> getCurrencyByStatus(Long countryId, String status, String role) {
//        List<Object[]> objects = new ArrayList<>();
        List<CurrencyByStatus> currencyByStatusList = new ArrayList<>();

        if (role == "ADMIN") {
            currencyByStatusList = medalRepository.currencyByStatus(status, countryId, null);
//            objects = medalRepository.currencyByStatus(status, countryId);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        } else {
            currencyByStatusList = medalRepository.currencyByStatus(status, countryId, true);
//            objects = medalRepository.currencyByStatus(status, countryId, true);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        }
        return currencyByStatusList;
    }

    @Override
    public Page<Medal> findMedalPaginated(Integer pageNo, Integer pageSize, Long currencyId, String status, String role) {
        List<Medal> medals = new ArrayList<>();
        if (role == "ADMIN") {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.medalRepository.medalPageable(currencyId, status, null, pageable);
        } else {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.medalRepository.medalPageable(currencyId, status, true, pageable);
        }
    }
}
