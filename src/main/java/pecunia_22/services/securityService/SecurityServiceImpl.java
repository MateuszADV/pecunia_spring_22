package pecunia_22.services.securityService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pecunia_22.models.Note;
import pecunia_22.models.Security;
import pecunia_22.models.repositories.SecurityRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.models.sqlClass.GetSecuritiesByStatus;
import pecunia_22.timing.annotation.MeasureTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private SecurityRepository securityRepository;

    @Override
    public List<Security> getAllSecurity() {
        return this.securityRepository.findAll();
    }

    @Override
    public List<Security> getAllSecurityOrderByID() {
        return this.securityRepository.getAllSecurityOrderById();
    }

    @Override
    public Security getSecurityById(Long id) {
        Optional<Security> security = securityRepository.findById(id);
        if (security.isPresent()) {
            return security.get();
        } else {
            throw new RuntimeException("Security Not Found From id :: " + id);
        }
    }

    @Override
    public Security saveSecurity(Security security) {
        return this.securityRepository.save(security);
    }

    @Override
    public void deleteSecurityById(Long id) {
        if (securityRepository.existsById(id)) {
            this.securityRepository.deleteById(id);
        } else {
            throw new RuntimeException("Record by Id - " + id + " not exist");
        }
    }

    @Override
    public List<Security> getSecurityByCurrencyId(Long currencyId) {
        List<Security> securities = securityRepository.getSecurityByCurrencyId(currencyId);
        return securities;
    }

    @Override
    public List<GetSecuritiesByStatus> getSecurityByStatus(String status) {
        List<Object[]> objects = securityRepository.getSecuritiesByStatus(status, null, null);
        List<GetSecuritiesByStatus> getSecuritiesByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getSecuritiesByStatusList.add(new ModelMapper().map(object[0], GetSecuritiesByStatus.class));
        }
        System.out.println("------------------SECURITIES FOR SELL--------------------------------");
        System.out.println(getSecuritiesByStatusList);
        System.out.println("------------------SECURITIES FOR SELL--------------------------------");
        return getSecuritiesByStatusList;
    }

    @Override
    public List<CountryByStatus> getCountryByStatus(String status, String role) {
        List<Object[]> objects = new ArrayList<>();
        List<CountryByStatus> countryByStatusList = new ArrayList<>();

        if (role == "ADMIN") {
            countryByStatusList = securityRepository.countryByStatus(status, null);
//            objects = securityRepository.countryByStatus(status);
//            for (Object[] object : objects) {
//                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
//            }
        } else {
            countryByStatusList = securityRepository.countryByStatus(status, true);
//            objects = securityRepository.countryByStatus(status, true);
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
            currencyByStatusList = securityRepository.currencyByStatus(status, countryId, null);
//            objects = securityRepository.currencyByStatus(status, countryId);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        } else {
            currencyByStatusList = securityRepository.currencyByStatus(status, countryId, true);
//            objects = securityRepository.currencyByStatus(status, countryId, true);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        }
        return currencyByStatusList;
    }

    @Override
    public Page<Security> findSecurityPaginated(Integer pageNo, Integer pageSize, Long currencyId, String status, String role) {
        List<Security> securities = new ArrayList<>();
        if (role == "ADMIN") {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.securityRepository.securityPageable(currencyId, status, null, pageable);
        } else {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.securityRepository.securityPageable(currencyId, status, true, pageable);
        }
    }

    @MeasureTime(value = "Update security in DB", color = MeasureTime.ConsoleColor.GREEN)
//    @MeasureTime("Update note in DB")
    @Override
    @Transactional
    public void updateSecurity(Security security) {
        securityRepository.updateSecurity(security);
    }

//    @Override
//    public void updateSecurity(Security security) {
//        securityRepository.updateSecurity(security.getCurrencies().getId(), security.getDenomination(), security.getDateBuy(), security.getNameCurrency(), security.getSeries(),
//                security.getBoughts().getId(), security.getItemDate(), security.getQuantity(), security.getUnitQuantity(), security.getActives().getId(), security.getPriceBuy(), security.getPriceSell(),
//                security.getMakings().getId(), security.getQualities().getId(), security.getWidth(), security.getHeight(), security.getStatuses().getId(), security.getImageTypes().getId(),
//                security.getStatusSell(), security.getVisible(), security.getDescription(), security.getAversPath(), security.getReversePath(), security.getSerialNumber(),
//                security.getId());
//    }
}
