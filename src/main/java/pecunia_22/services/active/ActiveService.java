package pecunia_22.services.active;

import org.springframework.stereotype.Service;
import pecunia_22.models.Active;

import java.util.List;

@Service
public interface ActiveService {

    List<Active> getAllActive();
    void saveActive(Active active);
    Active getActiveById(Long id);
    void deleteActiveById(Long id);
    Boolean activeCodExist(Integer activeCod);

    Active getActiveByActiveCod(Integer activeCod);
}
