package pecunia_22.services.medalService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Medal;

import java.util.List;

@Service
public interface MedalService {
    List<Medal> getAllMedal();
    void saveMedal(Medal medal);
    Medal getMedalById(Long id);
    void deleteMedalById(Long id);

    List<Medal> getMedalByCurrencyId(Long currencyId);

    void updateMedal(Medal medal);
}
