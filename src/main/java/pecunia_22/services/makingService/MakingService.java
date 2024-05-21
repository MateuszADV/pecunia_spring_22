package pecunia_22.services.makingService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Making;

import java.util.List;

@Service
public interface MakingService {
    List<Making> getAllMakings();
    void saveMaking(Making making);
    Making getMakingById(Long id);
    void deleteMakingById(Long id);
}
