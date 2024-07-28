package pecunia_22.services.securityService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pecunia_22.models.Note;
import pecunia_22.models.Security;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.models.sqlClass.GetSecuritiesByStatus;

import java.util.List;

@Service
public interface SecurityService {

    List<Security> getAllSecurity();
    List<Security> getAllSecurityOrderByID();
    Security getSecurityById(Long id);
    Security saveSecurity(Security security);
    void deleteSecurityById(Long id);

    List<Security> getSecurityByCurrencyId(Long currencyId);

    List<GetSecuritiesByStatus> getSecurityByStatus(String status);

    //    List<CountryByStatus> getCountryByStatus(String status);
    List<CountryByStatus> getCountryByStatus(String status, String role);
    List<CurrencyByStatus> getCurrencyByStatus(Long countryId, String status, String role);
    Page<Security> findSecurityPaginated(Integer pageNo, Integer pageSize, Long currencyId, String status, String role);

    void updateSecurity(Security security);
}
